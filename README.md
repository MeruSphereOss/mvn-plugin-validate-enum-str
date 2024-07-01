# Maven PlugIn :: validate-enum-str
This PlugiN will Validate Enum attributes in the Hibernate Model Class have Enumerated String annotation or not

## Problem Statement

## Solution


# How to use this PlugIn in your project

To use this, Include the below in your project plugins section of pom.xml

```
			<plugin>
				<groupId>com.merusphere.devops</groupId>
				<artifactId>mvnplugin.validate-enum-str</artifactId>
				<version>0.9.1</version>
				<executions>
					<execution>
						<id>compile</id>
						<goals>
							<goal>compile</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<pkg>com.your.project</pkg>
				</configuration>
			</plugin>
```

## Configuration Parameters
* pkg : Name of the Java Package contains all the Hibernate Model Classes
		* Default value : No Default value
		* Mandatory : Mandatory
		* Description : 
		* Example : 
	


# How to develop this PlugIn based on your requirements

Please follow the below steps to set up the Development Environment in your system
## Pre-rqisites
+ Open JDK 11
+ Maven
+ pgp latest version
+ PGP Private and Public Keys. Get these keys from the github repository 
Development environment set up instructions can be found at [Github Repo](https://github.com/MeruSphereOss/mvn-plugin-dev-setup.git)

## Eclipse or STS
+ Project enviornment should be Open JDK 11

## Source code repository
Checkout the code from [validate-enum-str](https://github.com/MeruSphereOss/mvn-plugin-validate-enum-str)

## GPG/PGP Keys setup
+ Checkout the GPG/PGP Keys from the [Github Repo](https://github.com/MeruSphereOss/mvn-plugin-dev-setup.git) and import into the local system

## Branch & Release


## Release notes
Please refer to https://github.com/MeruSphereOss/mvn-plugin-validate-enum-str/releases

## Report Bugs
Please report all the issues to Gitub issues https://github.com/MeruSphereOss/mvn-plugin-validate-enum-str/issues

## Contribution
Please finish your development and create branch then Please crate a Github Issue at Gitub issues https://github.com/MeruSphereOss/mvn-plugin-validate-enum-str/issues with the feature details & branch details. One of the Maintainer will co-ordinate with you.

