package com.merusphere.devops.mvnplugin;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;

/**
 * Checks that whether Enum Field Types in model classes contain Enumerated
 * Annotation or not and also checks that for enum fields containing enumerated
 * annotation, whether enumerated annotation contains Enum String Value or not
 */
@Mojo(name = "validate-enum-str", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class EnumeratedAnnotationCheckerMojo extends AbstractMojo {

	/**
	 * Scan the classes from the Package name from the configuration parameter named
	 * pkg
	 */
	@Parameter(property = "pkg")
	String pkg;

	/**
	 * Gives access to the Maven project information.
	 */
	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	MavenProject project;

	/**
	 * Checks the number of methods not having Security Annotations
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			if (pkg == null || pkg.trim().length() <= 0) {
				new MojoExecutionException("Package name can't be null for the project : " + project.getName());
			}

			if (getLog().isInfoEnabled()) {
				getLog().info(new StringBuffer("Using Package " + pkg));
			}

			// Load the Class File information
			Set<URL> urls = new HashSet<>();
			List<String> testElements = project.getTestClasspathElements();
			for (String element : testElements) {
				urls.add(new File(element).toURI().toURL());
			}
			List<String> runtimeElements = project.getRuntimeClasspathElements();
			for (String element : runtimeElements) {
				urls.add(new File(element).toURI().toURL());
			}
			List<String> compiletimeElements = project.getCompileClasspathElements();
			for (String element : compiletimeElements) {
				urls.add(new File(element).toURI().toURL());
			}
			List<String> systemtimeElements = project.getSystemClasspathElements();
			for (String element : systemtimeElements) {
				urls.add(new File(element).toURI().toURL());
			}
			Set<Artifact> artifactSet = project.getArtifacts();
			for (Artifact artft : artifactSet) {
				urls.add(artft.getFile().toURI().toURL());
			}

			if (getLog().isInfoEnabled()) {
				getLog().info("Classpath Found " + urls.size() + " Classses");
			}
			for (URL url : urls) {
				getLog().info("URL file " + url.getFile());
			}

			// Load the Classpath
			ClassLoader contextClassLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]),
					Thread.currentThread().getContextClassLoader());

			Thread.currentThread().setContextClassLoader(contextClassLoader);

			Reflections reflections = new Reflections(pkg);
			Set<Class<?>> clazzSet = reflections.getTypesAnnotatedWith(Table.class, true);

			if (clazzSet == null || clazzSet.isEmpty()) {
				if (getLog().isInfoEnabled()) {
					getLog().info("No classes Found with Table Annotation");
				}
				return;
			}

			List<Class<?>> checkedClazzList = new ArrayList<>();
			List<String> enumeratedAnnotationAbsenceList = new ArrayList<>();
			List<String> enumeratedStringAbsenceList = new ArrayList<>();

			for (Class<?> clazz : clazzSet) {
				Class<?> currentClazz = clazz;
				while (currentClazz != null) {
					if (!checkedClazzList.contains(currentClazz)) {
						Field[] fields = currentClazz.getDeclaredFields();
						if (fields != null) {
							for (Field field : fields) {
								boolean hasColumnAnn = field.isAnnotationPresent(Column.class);
								if (hasColumnAnn && field.getType().isEnum()) {
									if (!field.isAnnotationPresent(Enumerated.class)) {
										enumeratedAnnotationAbsenceList.add("Class: " + currentClazz.getCanonicalName()
												+ " -- " + "Field: " + field.getName());
									} else {
										Enumerated enumerated = field.getAnnotation(Enumerated.class);
										if (enumerated.value() != EnumType.STRING) {
											enumeratedStringAbsenceList.add("Class: " + currentClazz.getCanonicalName()
													+ " -- " + "Field: " + field.getName());
										}
									}
								}
							}
						}
						checkedClazzList.add(currentClazz);
					}
					currentClazz = currentClazz.getSuperclass();
				}
			}

			// Print Error List
			if (enumeratedAnnotationAbsenceList != null && !enumeratedAnnotationAbsenceList.isEmpty()) {
				getLog().error("Enum Fields does not contain Enumerated Annotation");
				for (String error : enumeratedAnnotationAbsenceList) {
					getLog().error(error);
				}
			}

			if (enumeratedStringAbsenceList != null && !enumeratedStringAbsenceList.isEmpty()) {
				getLog().error(
						"\n\nEnum Fields containing Enumerated Annotation but Enumerated Annotation does not contain EnumType String Value");
				for (String error : enumeratedStringAbsenceList) {
					getLog().error(error);
				}
			}

			if (!enumeratedAnnotationAbsenceList.isEmpty() || !enumeratedStringAbsenceList.isEmpty()) {
				throw new MojoExecutionException("Found Enumerated Errors in the Model Classes");
			}
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
