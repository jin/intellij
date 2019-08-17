/*
 * Copyright 2019 The Bazel Authors. All rights reserved.
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
package com.google.idea.blaze.aspect.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import build.bazel.tests.integration.BazelCommand;
import build.bazel.tests.integration.WorkspaceDriver;
import java.io.IOException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A Bazel-invoking integration test for the bundled IntelliJ aspect.
 *
 * These tests assert the end-to-end behavior of the plugin's aspect during a sync, and
 * ensure that it generates the correct IDE info files.
 */
public class BazelInvokingIntegrationTest {

  private WorkspaceDriver driver = new WorkspaceDriver();

  @BeforeClass
  public static void setUpClass() throws IOException {
    WorkspaceDriver.setUpClass();
  }

  @Before
  public void setUp() throws Exception {
    driver.setUp();
  }

  @Test
  public void aspect_intelliJInfoGenericOutputGroup_generatesInfoTxt() throws Exception {
    driver.scratchFile("foo/BUILD", "sh_test(name = \"bar\",\n" + "srcs = [\"bar.sh\"])");
    driver.scratchExecutableFile("foo/bar.sh", "echo \"bar\"", "exit 0");

    BazelCommand cmd = driver.bazel(
        "build",
        "//foo:bar",
        "--repository_cache=\"\"",
        "--override_repository=intellij_aspect="
            + System.getenv("TEST_SRCDIR")
            + "/intellij_with_bazel/aspect",
        "--define=ij_product=intellij-latest",
        "--aspects=@intellij_aspect//:intellij_info_bundled.bzl%intellij_info_aspect",
        "--output_groups=intellij-info-generic"
    ).runVerbose();

    assertEquals("return code is 0", 0, cmd.exitCode());
    assertTrue(
        "stderr contains intellij-info.txt",
        cmd.errorLines().stream().anyMatch(x -> x.contains("intellij-info.txt")));
  }

}
