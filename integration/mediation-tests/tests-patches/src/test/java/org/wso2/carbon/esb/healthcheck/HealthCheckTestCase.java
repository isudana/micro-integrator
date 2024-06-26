/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.esb.healthcheck;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.test.utils.http.client.HttpResponse;
import org.wso2.carbon.automation.test.utils.http.client.HttpURLConnectionClient;
import org.wso2.esb.integration.common.utils.ESBIntegrationTest;


import java.io.IOException;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;


public class HealthCheckTestCase extends ESBIntegrationTest {

    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {
        super.init();
    }

    @AfterClass(alwaysRun = true)
    public void destroy() throws Exception {
        super.cleanup();
    }

    @Test(groups = "wso2.esb", description = "invoke service - HealthCheck")
    public void testHeathCheckAPI()
            throws IOException {

        HttpResponse response = HttpURLConnectionClient.sendGetRequest("http://" + getHostname() +
                ":9391/liveness", null, "application/json");
        assertNotNull(response, "Response is null");
        //should return the response without throwing any errors.
        assertEquals(response.getResponseCode(), 200);
    }
}
