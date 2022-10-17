pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS=credentials('dockerhub-oliverpm')
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
                echo 'Checkout App Hola Ripley'
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'github-oliverpm', url: 'https://github.com/oliverpm/app-ripleydemo.git']]])
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
                sh "sshpass -p AdrikCedrik.7 scp Dockerfile administrador@192.168.18.66:/home/administrador/ripley"
                sh "sshpass -p AdrikCedrik.7 scp target/HolaRipley-0.0.1-SNAPSHOT.jar administrador@192.168.18.66:/home/administrador/ripley/target"
                sh "sshpass -p AdrikCedrik.7 ssh -t administrador@192.168.18.66 'cd ripley; docker build . -t oliverpm/hola-ripley'"
            }
        }
        stage('Push Image To Docker Hub') {
            steps {
                echo 'Push Image To Docker Hub'
                sh "sshpass -p AdrikCedrik.7 ssh -t administrador@192.168.18.66 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin' "
                sh "sshpass -p AdrikCedrik.7 ssh -t administrador@192.168.18.66 'docker push oliverpm/hola-ripley:latest' "
            }
        }
        stage('Deploy to Minukube') {
            steps {
                echo 'Deploy App to Minukube'
                sh "sshpass -p AdrikCedrik.7 scp deploymentServiceRipley.yml administrador@192.168.18.66:/home/administrador/ripley"
                sh "sshpass -p AdrikCedrik.7 ssh -t administrador@192.168.18.66 'cd ripley; kubectl apply -f deploymentServiceRipley.yml' "
            }
        }
        stage('Test App Hola Ripley') {
            steps {
		  sh "sshpass -p AdrikCedrik.7 scp testAppDemoRipley.sh administrador@192.168.18.66:/home/administrador/ripley"
		  sh "sshpass -p AdrikCedrik.7 ssh -t administrador@192.168.18.66  ' cd ripley ; chmod testAppDemoRipley.sh ' "  
		  script{
                    for (int i = 1; i <= 6; i++){
			echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Test Demo Ripley  ${i} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"    
                        sh "sshpass -p AdrikCedrik.7 ssh -t administrador@192.168.18.66  ' cd ripley ; sh  testAppDemoRipley.sh ' "
                        sleep(2)
                    }
             }		    
         }
        
      }

    
    }

    /*
    post {
		always {
            echo 'docker logout'
			//sh 'docker logout'
		}
	}
    */
}
