/*
 * Copyright 2015 Sir Wellington.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sir.wellington.alchemy.http;

import com.google.common.base.Strings;
import java.net.URL;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static sir.wellington.alchemy.arguments.Arguments.checkThat;
import static sir.wellington.alchemy.arguments.Assertions.greaterThanOrEqualTo;
import static sir.wellington.alchemy.arguments.Assertions.nonEmptyString;
import static sir.wellington.alchemy.arguments.Assertions.not;
import static sir.wellington.alchemy.arguments.Assertions.notNull;
import static sir.wellington.alchemy.arguments.Assertions.sameInstance;
import sir.wellington.alchemy.collections.maps.Maps;
import sir.wellington.alchemy.http.exceptions.AlchemyHttpException;
import sir.wellington.alchemy.http.operations.HttpOperation;
import sir.wellington.alchemy.http.operations.HttpRequest;

/**
 *
 * @author SirWellington
 */
class Step2Impl implements HttpOperation.Step2
{

    private final static Logger LOG = LoggerFactory.getLogger(Step2Impl.class);

    private HttpRequest request;

    private final AlchemyHttpStateMachine stateMachine;

    Step2Impl(AlchemyHttpStateMachine stateMachine, HttpRequest request)
    {
        checkThat(request).is(notNull());
        checkThat(stateMachine).is(notNull());
        this.stateMachine = stateMachine;
        this.request = request;
    }

    @Override
    public HttpOperation.Step2 usingHeader(String key, String value) throws IllegalArgumentException
    {
        checkThat(key).usingMessage("missing key").is(nonEmptyString());
        //Value of an HTTP Header can be empty ?
        value = Strings.nullToEmpty(value);

        Map<String, String> requestHeaders = Maps.nullToEmpty(request.getRequestHeaders());
        requestHeaders.put(key, value);

        this.request = HttpRequest.Builder.from(request)
                .usingRequestHeaders(requestHeaders)
                .build();

        return this;
    }

    @Override
    public HttpOperation.Step2 followRedirects(int maxNumberOfTimes) throws IllegalArgumentException
    {
        checkThat(maxNumberOfTimes).is(greaterThanOrEqualTo(1));
        //TODO:
        //Not doing anything with this yet.
        return this;
    }

    @Override
    public HttpResponse at(URL url) throws AlchemyHttpException
    {
        //Ready to do a sync request
        return stateMachine.executeSync(request);
    }

    @Override
    public HttpOperation.Step4<HttpResponse> onSuccess(HttpOperation.OnSuccess<HttpResponse> onSuccessCallback)
    {

        checkThat(onSuccessCallback)
                .usingMessage("callback cannot be null")
                .is(notNull());

        return stateMachine.getStep4(request, HttpResponse.class, onSuccessCallback);
    }

    @Override
    public <ResponseType> HttpOperation.Step3<ResponseType> expecting(Class<ResponseType> classOfResponseType) throws IllegalArgumentException
    {
        checkThat(classOfResponseType)
                .usingMessage("missing class of response type")
                .is(notNull())
                .usingMessage("cannot expect Void")
                .is(not(sameInstance(Void.class)));

        return stateMachine.getStep3(request, classOfResponseType);
    }

}