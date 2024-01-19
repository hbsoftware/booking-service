pipeline {
	agent any

	environment {
        MAVEN_HOME = "C:\\apache-maven-3.9.1"
		mavenHome = tool 'jenkins-maven'
	}

	tools {
		jdk 'java-17'
	}

	stages {

		stage('Build'){
			steps {
				bat "mvn clean install -DskipTests"
			}
		}

		stage('Test'){
			steps{
				bat "mvn test"
			}
		}

		stage('oc:build') {
			steps {
			    bat "mvn oc:build -Popenshift"
			}
		}
        stage('oc:resource') {
			steps {
			    bat "mvn oc:resource -Popenshift"
			}
		}
        stage('oc:apply') {
			steps {
			    bat "mvn oc:apply -Popenshift"
			}
		}
	}
}