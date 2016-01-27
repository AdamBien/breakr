/*
 * Copyright 2016 Adam Bien.
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
package com.airhacks.breakr;

/*
 * #%L
 * breakr
 * %%
 * Copyright (C) 2015 - 2016 Adam Bien
 * %%
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
 * #L%
 */
import javax.inject.Inject;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author airhacks.com
 */
@RunWith(CdiTestRunner.class)
public class BreakTest {

    @Inject
    CustomCircuit circuit;

    @Inject
    Unstable unstable;

    @Test
    public void configurableOpening() {
        long counter = circuit.getFailureCount();
        assertThat(counter, is(0l));

        unstable.tooSlow();
        assertThat(circuit.getFailureCount(), is(1l));

        unstable.tooSlow();
        assertThat(circuit.getFailureCount(), is(2l));

        //opening (method no more invoked)
        circuit.setOpen(true);
        unstable.tooSlow();
        assertThat(circuit.getFailureCount(), is(2l));

        //opening (method no more invoked)
        circuit.setOpen(false);
        unstable.tooSlow();
        assertThat(circuit.getFailureCount(), is(3l));

    }

}
