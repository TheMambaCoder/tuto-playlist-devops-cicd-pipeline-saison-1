pipeline {

    agent any    
    
    environment {
   
    PASS = credentials('PASSWORD_DOCKER_HUB')

   }

   
    stages {

        stage('Pull du code de la branch selectionner') {
            steps {
                sh "echo '### Pull du code de la branch ####' "
            
		checkout scmGit(branches: [[name: '$BRANCH']], extensions: [], userRemoteConfigs: [[credentialsId: 'GITLAB_CREDENTIALS', url: 'http://gitlab.example.com/jenkins-pipeline/pipeline-project.git']])
                }

        }

        stage('Build') {
            steps {
                sh "echo '### build stage ####' "
		sh "./pipeline-sh-scripts/build/mvn-cmd.sh mvn -B -DskipTests clean package"		
            }

        }

        stage('Test') {
            steps {
                sh "echo '### Test stage- execution des tests de l app ####' "

		sh "./pipeline-sh-scripts/build/mvn-cmd.sh mvn test"

		
            }

        }



	   stage('Pushing_docker_image') {
            steps { 
                sh "echo '### creation docker-image et Push vers le registry ####' "

		sh "./pipeline-sh-scripts/build/build-docker-image.sh"
       
		sh "./pipeline-sh-scripts/push/push-docker-image.sh"

	    }

        }

       stage('Deploy with Ansible') {
           steps { 

		script {
                if (env.ENV_TO_DEPLOY == "Dev") {

                sh "echo '### Deploying with ansible vers DEV ####' "
						
	ansiblePlaybook (
              colorized: true,
              playbook: "/var/lib/jenkins/workspace/my_pipeline_project/ansible-deploy-app/playbook-dev.yml",
              hostKeyChecking: false,
              inventory: "/var/lib/jenkins/workspace/my_pipeline_project/ansible-deploy-app/inventory",
              extras: "-e 'docker_image_tag=$BUILD_TAG'"
              )

        	} // fin du if 

                if (env.ENV_TO_DEPLOY == "Stage") {

                sh "echo '### Deploying with ansible vers Stage ####' "
						
	ansiblePlaybook (
              colorized: true,
              playbook: "/var/lib/jenkins/workspace/my_pipeline_project/ansible-deploy-app/playbook-stage.yml",
              hostKeyChecking: false,
              inventory: "/var/lib/jenkins/workspace/my_pipeline_project/ansible-deploy-app/inventory",
              extras: "-e 'docker_image_tag=$BUILD_TAG'"
              )

        	} // fin du if 


                if (env.ENV_TO_DEPLOY == "Prod") {

                sh "echo '### Deploying with ansible vers Prod ####' "
						
	ansiblePlaybook (
              colorized: true,
              playbook: "/var/lib/jenkins/workspace/my_pipeline_project/ansible-deploy-app/playbook-prod.yml",
              hostKeyChecking: false,
              inventory: "/var/lib/jenkins/workspace/my_pipeline_project/ansible-deploy-app/inventory",
              extras: "-e 'docker_image_tag=$BUILD_TAG'"
              )

        	} // fin du if 
           }
         
} // fin bloc script 
       }


    }
}
