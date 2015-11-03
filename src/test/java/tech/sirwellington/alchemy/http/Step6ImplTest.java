/*
 * Copyright 2015 SirWellington Tech.
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
package tech.sirwellington.alchemy.http;

import java.net.URL;
import org.inferred.freebuilder.shaded.com.google.common.base.Strings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Mock;
import tech.sirwellington.alchemy.http.AlchemyRequest.Step6;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.http.Generators.validUrls;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class Step6ImplTest
{

    @Mock
    private AlchemyHttpStateMachine stateMachine;

    @Mock
    private HttpRequest request;

    @Captor
    private ArgumentCaptor<HttpRequest> requestCaptor;

    @Mock
    private AlchemyRequest.OnSuccess onSuccess;

    @Mock
    private AlchemyRequest.OnFailure onFailure;

    private Class<?> responseClass;

    private Step6 instance;

    private URL url;

    @Before
    public void setUp()
    {
        responseClass = Integer.class;
        url = one(validUrls());
        
        instance = new Step6Impl(stateMachine, request, responseClass, onSuccess, onFailure);
        verifyZeroInteractions(stateMachine, request, onSuccess, onFailure);
    }

    @Test
    public void testConstructor()
    {
        //Edge cases
        assertThrows(() -> new Step6Impl(null, null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> new Step6Impl(stateMachine, null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> new Step6Impl(stateMachine, request, responseClass, null, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> new Step6Impl(stateMachine, request, responseClass, onSuccess, null))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void testAt()
    {
        //Edge cases
        assertThrows(() -> instance.at(""))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> instance.at((URL) null))
                .isInstanceOf(IllegalArgumentException.class);

        instance.at(url);

        HttpRequest expectedRequest = HttpRequest.Builder.from(request)
                .usingUrl(url)
                .build();

        verify(stateMachine).executeAsync(expectedRequest, responseClass, onSuccess, onFailure);
    }

    @Test
    public void testToString()
    {
        String toString = instance.toString();
        assertThat(Strings.isNullOrEmpty(toString), is(false));
    }

}