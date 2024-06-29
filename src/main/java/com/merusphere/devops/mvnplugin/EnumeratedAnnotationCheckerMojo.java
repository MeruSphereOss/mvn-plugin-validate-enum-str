package com.merusphere.devops.mvnplugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

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
@Mojo(name = "validate-enum-str", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.TEST)
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
				new MojoExecutionException("Found Enumerated Errors in the Model Classes");
			}
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			new MojoExecutionException(e.getMessage(), e);
		}
	}

}
