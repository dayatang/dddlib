### 向job添加人员的api

http://localhost:8080/jenkins/job/sss/descriptorByName/hudson.security.AuthorizationMatrixProperty/checkName?value=%5Btttt%5D

### 基于设置安全方面时，服务器端接收的json数 据


    json: {
      "useSecurity": {
        "slaveAgentPort": {
          "value": "",
          "type": "random"
        },
        "disableRememberMe": false,
        "": "0",
        "markupFormatter": {
          "stapler-class": "hudson.markup.RawHtmlMarkupFormatter",
          "disableSyntaxHighlighting": false
        },
        "realm": {
          "value": "0",
          "stapler-class": "org.jenkinsci.plugins.cas.CasSecurityRealm",
          "casServerUrl": "http://localhost:8080/cas/",
          "": "1",
          "casProtocol": {
            "stapler-class": "org.jenkinsci.plugins.cas.protocols.Cas20Protocol",
            "authoritiesAttribute": "groups,roles",
            "fullNameAttribute": "cn",
            "emailAttribute": "mail",
            "proxyEnabled": false,
            "proxyAllowAny": "false",
            "proxyAllowList": ""
          },
          "forceRenewal": false,
          "enableSingleSignOut": true
        },
        "authorization": {
          "value": "4",
          "stapler-class": "hudson.security.ProjectMatrixAuthorizationStrategy",
          "data": {
            "anonymous": {
              "hudson.model.Hudson.Administer": false,
              "hudson.model.Hudson.Read": false,
              "hudson.model.Hudson.RunScripts": false,
              "hudson.model.Hudson.UploadPlugins": false,
              "hudson.model.Hudson.ConfigureUpdateCenter": false,
              "com.cloudbees.plugins.credentials.CredentialsProvider.Create": false,
              "com.cloudbees.plugins.credentials.CredentialsProvider.Update": false,
              "com.cloudbees.plugins.credentials.CredentialsProvider.View": false,
              "com.cloudbees.plugins.credentials.CredentialsProvider.Delete": false,
              "com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains": false,
              "hudson.model.Computer.Configure": false,
              "hudson.model.Computer.Delete": false,
              "hudson.model.Computer.Create": false,
              "hudson.model.Computer.Disconnect": false,
              "hudson.model.Computer.Connect": false,
              "hudson.model.Computer.Build": false,
              "hudson.model.Item.Create": false,
              "hudson.model.Item.Delete": false,
              "hudson.model.Item.Configure": false,
              "hudson.model.Item.Read": false,
              "hudson.model.Item.Discover": false,
              "hudson.model.Item.Build": false,
              "hudson.model.Item.Workspace": false,
              "hudson.model.Item.Cancel": false,
              "hudson.model.Run.Delete": false,
              "hudson.model.Run.Update": false,
              "hudson.model.View.Create": false,
              "hudson.model.View.Delete": false,
              "hudson.model.View.Configure": false,
              "hudson.model.View.Read": false,
              "hudson.scm.SCM.Tag": false
            },
            "admin": {
              "hudson.model.Hudson.Administer": true,
              "hudson.model.Hudson.Read": false,
              "hudson.model.Hudson.RunScripts": false,
              "hudson.model.Hudson.UploadPlugins": false,
              "hudson.model.Hudson.ConfigureUpdateCenter": false,
              "com.cloudbees.plugins.credentials.CredentialsProvider.Create": false,
              "com.cloudbees.plugins.credentials.CredentialsProvider.Update": false,
              "com.cloudbees.plugins.credentials.CredentialsProvider.View": false,
              "com.cloudbees.plugins.credentials.CredentialsProvider.Delete": false,
              "com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains": false,
              "hudson.model.Computer.Configure": false,
              "hudson.model.Computer.Delete": false,
              "hudson.model.Computer.Create": false,
              "hudson.model.Computer.Disconnect": false,
              "hudson.model.Computer.Connect": false,
              "hudson.model.Computer.Build": false,
              "hudson.model.Item.Create": false,
              "hudson.model.Item.Delete": false,
              "hudson.model.Item.Configure": false,
              "hudson.model.Item.Read": false,
              "hudson.model.Item.Discover": false,
              "hudson.model.Item.Build": false,
              "hudson.model.Item.Workspace": false,
              "hudson.model.Item.Cancel": false,
              "hudson.model.Run.Delete": false,
              "hudson.model.Run.Update": false,
              "hudson.model.View.Create": false,
              "hudson.model.View.Delete": false,
              "hudson.model.View.Configure": false,
              "hudson.model.View.Read": false,
              "hudson.scm.SCM.Tag": false
            }
          },
          "": "admin"
        }
      },
      "hudson-security-csrf-GlobalCrumbIssuerConfiguration": {

      },
      "core:apply": "true"
    }




    json: {
      "name": "sss",
      "description": "",
      "": [
        "",
        "0"
      ],
      "logrotate": false,
      "buildDiscarder": {
        "stapler-class": "hudson.tasks.LogRotator",
        "daysToKeepStr": "",
        "numToKeepStr": "",
        "artifactDaysToKeepStr": "",
        "artifactNumToKeepStr": ""
      },
      "properties": {
        "stapler-class-bag": "true",
        "hudson-security-AuthorizationMatrixProperty": {
          "useProjectSecurity": {
            "data": {
              "anonymous": {
                "hudson.model.Item.Delete": false,
                "hudson.model.Item.Configure": false,
                "hudson.model.Item.Read": false,
                "hudson.model.Item.Discover": false,
                "hudson.model.Item.Build": false,
                "hudson.model.Item.Workspace": false,
                "hudson.model.Item.Cancel": false,
                "hudson.model.Run.Delete": false,
                "hudson.model.Run.Update": false,
                "hudson.scm.SCM.Tag": false
              },
              "tttt": {
                "hudson.model.Item.Delete": true,
                "hudson.model.Item.Configure": true,
                "hudson.model.Item.Read": true,
                "hudson.model.Item.Discover": true,
                "hudson.model.Item.Build": true,
                "hudson.model.Item.Workspace": true,
                "hudson.model.Item.Cancel": true,
                "hudson.model.Run.Delete": true,
                "hudson.model.Run.Update": true,
                "hudson.scm.SCM.Tag": true
              }
            },
            "": "tttt"
          }
        },
        "hudson-model-ParametersDefinitionProperty": {

        }
      },
      "displayNameOrNull": "",
      "scm": {
        "value": "2"
      },
      "core:apply": "true"
    }