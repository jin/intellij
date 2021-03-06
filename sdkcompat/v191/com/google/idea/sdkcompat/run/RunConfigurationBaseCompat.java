/*
 * Copyright 2018 The Bazel Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.idea.sdkcompat.run;

import com.intellij.execution.BeforeRunTask;
import com.intellij.execution.RunManagerEx;
import com.intellij.execution.configurations.RunConfigurationBase;
import java.util.List;

/** SDK Compat for {@link com.intellij.execution.configurations.RunConfigurationBase}. */
public class RunConfigurationBaseCompat {
  /**
   * RunConfigurationBase#getBeforeRunTasks isn't reliable in 2018.1 -- it often returns an empty
   * list while the run configuration is being edited.
   *
   * <p>#api181: superclass method seems more reliable in 2018.2. Double-check and remove when we no
   * longer support 2018.1.
   */
  @SuppressWarnings("rawtypes")
  public static List<BeforeRunTask> getAllBeforeRunTasks(RunConfigurationBase config) {
    return RunManagerEx.getInstanceEx(config.getProject()).getBeforeRunTasks(config);
  }
}
