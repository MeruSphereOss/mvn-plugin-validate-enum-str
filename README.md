# Maven Plugin :: validate-enum-str
This Plugin will Validate Enum attributes in the Hibernate Model Class have Enumerated String annotation or not

## Problem Statement
This Plugin will Validate Enum attributes in the Hibernate Model Class have Enumerated String annotation or not

## Solution
This Plugin will Validate Enum attributes in the Hibernate Model Class have Enumerated String annotation or not

# How to use this Plugin in your project

To use this, Include the below in your project plugins section of pom.xml

```
			<plugin>
				<groupId>com.merusphere.devops</groupId>
				<artifactId>mvnplugin.validate-enum-str</artifactId>
				<version>0.9.4</version>
				<executions>
					<execution>
						<phase>compile</phase>
						<goals>
							<goal>validate-enum-str</goal>
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
		* Description : Name of the Java Package contains all the Hibernate Model Classes
		* Example : com.merusphere.dao.model
	
## How to run this plugin in your projects

Note : This PlugIn runs as part of the Maven Phase: compile
If you want to fire it, use the Goal : validate-enum-str

```
mvn clean compile
or
mvn clean install
```

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
Please finish your development and create branch then Please create a Github Issue at Gitub issues https://github.com/MeruSphereOss/mvn-plugin-validate-enum-str/issues with the feature details & branch details. One of the Maintainer will co-ordinate with you.

