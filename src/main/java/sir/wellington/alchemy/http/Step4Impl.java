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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static sir.wellington.alchemy.arguments.Arguments.checkThat;
import static sir.wellington.alchemy.arguments.Assertions.notNull;
import sir.wellington.alchemy.http.operations.HttpOperation;
import sir.wellington.alchemy.http.operations.HttpRequest;

/**
 *
 * @author SirWellington
 */
class Step4Impl<ResponseType> implements HttpOperation.Step4<ResponseType>
{

    private final static Logger LOG = LoggerFactory.getLogger(Step4Impl.class);

    private final AlchemyHttpStateMachine stateMachine;
    private final HttpRequest request;
    private final Class<ResponseType> classOfResponseType;
    private final HttpOperation.OnSuccess<ResponseType> successCallback;

    public Step4Impl(AlchemyHttpStateMachine stateMachine,
                     HttpRequest request,
                     Class<ResponseType> classOfResponseType,
                     HttpOperation.OnSuccess<ResponseType> successCallback)
    {
        checkThat(stateMachine).is(notNull());
        checkThat(request).is(notNull());
        checkThat(classOfResponseType).is(notNull());
        checkThat(successCallback).is(notNull());

        this.stateMachine = stateMachine;
        this.request = request;
        this.classOfResponseType = classOfResponseType;
        this.successCallback = successCallback;
    }

    @Override
    public HttpOperation.Step5<ResponseType> onFailure(HttpOperation.OnFailure onFailureCallback)
    {
        checkThat(onFailureCallback)
                .usingMessage("callback cannot be null")
                .is(notNull());

        return stateMachine.getStep5(request, classOfResponseType, successCallback, onFailureCallback);
    }

    @Override
    public String toString()
    {
        return "Step4Impl{" + "stateMachine=" + stateMachine + ", classOfResponseType=" + classOfResponseType + ", successCallback=" + successCallback + '}';
    }

}