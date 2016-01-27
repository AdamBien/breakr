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
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
@Specializes
public class CustomCircuit extends Circuit {

    private boolean open;

    @Override
    public boolean isOpen(long maxFailures) {
        return open;
    }

    public long getFailureCount() {
        return failureCounter.get();
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}
