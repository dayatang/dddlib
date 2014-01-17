package org.openkoala.opencis.jenkins.configureImpl.scm;

import org.openkoala.opencis.jenkins.configureApi.ScmConfigStrategy;
import org.openkoala.opencis.jenkins.configureImpl.ProjectConfigUtil;
import org.openkoala.opencis.jenkins.util.SeleniumUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * User: zjzhai
 * Date: 1/9/14
 * Time: 10:44 AM
 */
public class GitConfig implements ScmConfigStrategy<WebDriver> {

    private String scmUrl;

    public GitConfig(String scmUrl) {
        this.scmUrl = scmUrl;
    }

    @Override
    public void config(WebDriver context) {


        String jobName = context.findElement(By.name("name")).getAttribute("value");

        List<WebElement> scmRadios = context.findElements(By.cssSelector("input[name=\"scm\"]"));
        String scm = "Git";
        WebElement selectedSCM = null;
        for (WebElement scmRadio : scmRadios) {
            WebElement label = scmRadio.findElement(By.xpath(".."));
            if (label.getText().contains(scm)) {
                selectedSCM = scmRadio;
                break;
            }
        }

        selectedSCM.click();

        WebElement repositoryUrlInput =
                context.findElement(
                        By.cssSelector("input[checkurl*=\"hudson.plugins.git.UserRemoteConfig\"]"));
        repositoryUrlInput.sendKeys(scmUrl);
        SeleniumUtil.clickBlankArea(context);


        repositoryUrlInput.click();

        ProjectConfigUtil.submitForm(context, jobName);


    }

}
