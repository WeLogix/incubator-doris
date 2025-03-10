// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.mysql.privilege;

import org.apache.doris.common.io.Text;
import org.apache.doris.common.io.Writable;
import org.apache.doris.persist.gson.GsonUtils;

import com.google.gson.annotations.SerializedName;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Used in
 */
public class CommonUserProperties implements Writable {
    @SerializedName("maxConn")
    private long maxConn = 100;
    @SerializedName("maxQueryInstances")
    private long maxQueryInstances = -1;
    @SerializedName("sqlBlockRules")
    private String sqlBlockRules = "";
    @SerializedName("cpuResourceLimit")
    private int cpuResourceLimit = -1;

    private String[] sqlBlockRulesSplit = {};

    long getMaxConn() {
        return maxConn;
    }

    long getMaxQueryInstances() {
        return maxQueryInstances;
    }

    String getSqlBlockRules() {
        return sqlBlockRules;
    }
    
    String[] getSqlBlockRulesSplit() {
        return sqlBlockRulesSplit;
    }

    void setMaxConn(long maxConn) {
        this.maxConn = maxConn;
    }

    void setMaxQueryInstances(long maxQueryInstances) {
        this.maxQueryInstances = maxQueryInstances;
    }

    void setSqlBlockRules(String sqlBlockRules) {
        this.sqlBlockRules = sqlBlockRules;
        setSqlBlockRulesSplit(sqlBlockRules);
    }
    
    void setSqlBlockRulesSplit(String sqlBlockRules) {
        // split
        this.sqlBlockRulesSplit = sqlBlockRules.replace(" ", "").split(",");
    }

    public int getCpuResourceLimit() {
        return cpuResourceLimit;
    }

    public void setCpuResourceLimit(int cpuResourceLimit) {
        this.cpuResourceLimit = cpuResourceLimit;
    }

    public static CommonUserProperties read(DataInput in) throws IOException {
        String json = Text.readString(in);
        CommonUserProperties commonUserProperties = GsonUtils.GSON.fromJson(json, CommonUserProperties.class);
        // trigger split
        commonUserProperties.setSqlBlockRulesSplit(commonUserProperties.getSqlBlockRules());
        return commonUserProperties;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        String json = GsonUtils.GSON.toJson(this);
        Text.writeString(out, json);
    }
}