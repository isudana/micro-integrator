/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.esb.mailto.transport.receiver.test;

import com.icegreen.greenmail.user.GreenMailUser;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.esb.integration.common.utils.CarbonLogReader;
import org.wso2.esb.integration.common.utils.ESBIntegrationTest;
import org.wso2.esb.integration.common.utils.Utils;
import org.wso2.esb.integration.common.utils.clients.GreenMailClient;
import org.wso2.esb.integration.common.utils.servers.GreenMailServer;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertTrue;

/**
 * This class is to test remove some header and preserve some headers to ESB from email
 */

public class MailToTransportPreserveHeadersTestCase extends ESBIntegrationTest {
    private static CarbonLogReader carbonLogReader;
    private static GreenMailClient greenMailClient;
    private static GreenMailUser greenMailUser;

    @BeforeClass(alwaysRun = true)
    public void initialize() throws Exception {
        super.init();
        OMElement mailToProxyOMElement = AXIOMUtil.stringToOM(FileUtils.readFileToString(new File(
                getESBResourceLocation() + File.separator + "mailTransport" + File.separator +
                        "mailTransportReceiver" + File.separator + "mail_transport_preserve_header.xml")));
        Utils.deploySynapseConfiguration(mailToProxyOMElement,
                "MailTransportPreserveHeader","proxy-services",
                true);
        carbonLogReader = new CarbonLogReader();
        greenMailUser = GreenMailServer.getPrimaryUser();
        greenMailClient = new GreenMailClient(greenMailUser);
        carbonLogReader.start();

        // Since ESB reads all unread emails one by one, we have to delete
        // the all unread emails before run the test
        GreenMailServer.deleteAllEmails("imap");
    }

    @Test(groups = { "wso2.esb" }, description = "Test email transport preserve header parameter")
    public void testEmailPreserveHeaderTransport() throws Exception {
        Date date = new Date();
        String emailSubject = "Preserve Headers Test : " + new Timestamp(date.getTime());
        Map<String, String> headers = new HashMap<>();
        headers.put("Delivered-To", "wso2@localhost");
        greenMailClient.sendMail(emailSubject, headers);

        assertTrue(carbonLogReader.checkForLog("Delivered-To = wso2@localhost", 10000),
                "Mail is not Received by ESB with Preserve Header Successfully");
    }

    @AfterClass(alwaysRun = true)
    public void deleteService() throws Exception {
        Utils.undeploySynapseConfiguration("MailTransportPreserveHeader","proxy-services", false);
        carbonLogReader.stop();
    }

}
