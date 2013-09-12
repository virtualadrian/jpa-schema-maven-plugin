package io.github.divinespear.maven.plugin;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JpaSchemaGeneratorMojoTest
        extends AbstractMojoTestCase {

    private static final String POM_FILENAME = "pom.xml";

    @Before
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @After
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private JpaSchemaGeneratorMojo findMojoFromPath(String path) throws Exception {
        File pomfile = new File(new File(getBasedir(), path), POM_FILENAME);
        // create mojo
        JpaSchemaGeneratorMojo mojo = (JpaSchemaGeneratorMojo) lookupMojo("generate", pomfile);
        assertThat(mojo, notNullValue(JpaSchemaGeneratorMojo.class));
        // configure project mock
        MavenProject projectMock = mock(MavenProject.class);
        when(projectMock.getCompileClasspathElements()).thenReturn(Arrays.asList(getBasedir() + "/target/test-classes"));
        setVariableValueToObject(mojo, "project", projectMock);

        return mojo;
    }

    @Test
    public void testGenerateScriptUsingEclipseLinkJPA21() throws Exception {
        JpaSchemaGeneratorMojo mojo = findMojoFromPath("target/test-classes/unit/eclipselink-jpa21-script-test");
        mojo.execute();
        // file check
        File createScriptFile = new File(mojo.getOutputDirectory(), mojo.getCreateOutputFileName());
        assertThat("create script should be generated.", createScriptFile.exists(), is(true));
        File dropScriptFile = new File(mojo.getOutputDirectory(), mojo.getDropOutputFileName());
        assertThat("drop script should be generated.", dropScriptFile.exists(), is(true));
    }
}
