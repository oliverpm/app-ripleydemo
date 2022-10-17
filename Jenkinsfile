pipeline {
    agent any

    environment {	    
	    def IP_HOST_LAB_DEMORIPLEY	="192.168.18.66"
        DOCKER_CREDENTIALS		=credentials('docker-oliverpm')        	    
        JENKINS_CREDENTIALS		=credentials('jenkins-oliverpm')	    
        DOCKERHUB_CREDENTIALS		=credentials('dockerhub-oliverpm')	    
	    MINIKUBE_CREDENTIALS		=credentials('minikube-oliverpm')
    }

    stages {
        stage('Clean Workspace') {
            steps {
                echo 'Cleaning..'
                cleanWs()

            }
        }
        stage('Checkout App Hola Ripley') {
            steps {
                echo 'Checkout App Hola Ripley: Branch $BRANCH_NAME'
                checkout([$class: 'GitSCM', branches: [[name: '*/$BRANCH_NAME']], extensions: [], userRemoteConfigs: [[credentialsId: 'github-oliverpm', url: 'https://github.com/oliverpm/app-ripleydemo.git']]])
            }
        }
        stage('Build App Maven') {
            steps {
                echo 'Build App Maven!!'
                sh "mvn clean install"
            }
        }
        stage('Build Docker Image') {
            steps {
		sh "sshpass -p '$DOCKER_CREDENTIALS_PSW' ssh -t $DOCKER_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY} 'mkdir -p ripley/$BRANCH_NAME; mkdir -p ripley/$BRANCH_NAME/target' ";    
                sh "sshpass -p '$DOCKER_CREDENTIALS_PSW' scp Dockerfile $DOCKER_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY}:/home/administrador/ripley/$BRANCH_NAME/"
                sh "sshpass -p '$DOCKER_CREDENTIALS_PSW' scp target/HolaRipley-0.0.1-SNAPSHOT.jar $DOCKER_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY}:/home/administrador/ripley/$BRANCH_NAME/target"
                sh "sshpass -p '$DOCKER_CREDENTIALS_PSW' ssh -t $DOCKER_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY} 'cd ripley/$BRANCH_NAME; docker build . -t oliverpm/hola-ripley'"
            }
        }
        stage('Push Image To Docker Hub') {
            steps {
                echo 'Push Image To Docker Hub'
                sh "sshpass -p '$DOCKER_CREDENTIALS_PSW' ssh -t $DOCKER_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY} 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin' "
                sh "sshpass -p '$DOCKER_CREDENTIALS_PSW' ssh -t $DOCKER_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY} 'docker push oliverpm/hola-ripley:latest' "
            }
        }
        stage('Deploy to Minukube') {
            steps {
                echo 'Deploy App to Minukube'
                sh "sshpass -p '$MINIKUBE_CREDENTIALS_PSW' scp deploymentServiceRipley.yml $MINIKUBE_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY}:/home/administrador/ripley"
                sh "sshpass -p '$MINIKUBE_CREDENTIALS_PSW' ssh -t $MINIKUBE_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY} 'cd ripley; kubectl apply -f deploymentServiceRipley.yml' "
            }
        }
        stage('Test App Hola Ripley') {
            steps {
		  sh "sshpass -p '$JENKINS_CREDENTIALS_PSW' scp testAppDemoRipley.sh $JENKINS_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY}:/home/administrador/ripley"
		  sh "sshpass -p '$JENKINS_CREDENTIALS_PSW' ssh -t $JENKINS_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY}  ' cd ripley ; chmod 777 testAppDemoRipley.sh ' "
		  script{
                    for (int i = 1; i <= 6; i++){
			            echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Test Demo Ripley  ${i} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                        sh "sshpass -p '$JENKINS_CREDENTIALS_PSW' ssh -t $JENKINS_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY}  ' cd ripley ; sh  testAppDemoRipley.sh ' "
                        sleep(2)
                    }
             }		    
         }
        
      }

    
    }

    
    post {
	always {
            echo 'docker logout'
		    sh "sshpass -p '$JENKINS_CREDENTIALS_PSW' ssh -t $JENKINS_CREDENTIALS_USR@${IP_HOST_LAB_DEMORIPLEY}  ' docker logout ' "
		}
	}
    
}
