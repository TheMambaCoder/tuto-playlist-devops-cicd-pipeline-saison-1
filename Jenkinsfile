pipeline {

    agent any    
   
    stages {

        stage('Build') {
            steps {
                sh "echo '### build stage ####' "		
            }

        }

        stage('Test') {
            steps {
                sh "echo '### Test stage- execution des tests de l app ####' "

            }

        }



	   stage('Pushing_docker_image') {
            steps { 
                sh "echo '### Pushing docker stage ####' "

           }

        }

       stage('Deploy with Ansible') {
           steps { 
                sh "echo '### Deploying with ansible ####' "

           }
         
       }


    }
}
