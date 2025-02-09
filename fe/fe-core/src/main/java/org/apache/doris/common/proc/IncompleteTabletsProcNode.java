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

package org.apache.doris.common.proc;

import org.apache.doris.catalog.Database;
import org.apache.doris.common.AnalysisException;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.Collections;

public class IncompleteTabletsProcNode implements ProcNodeInterface {
    public static final ImmutableList<String> TITLE_NAMES = new ImmutableList.Builder<String>()
            .add("UnhealthyTablets").add("InconsistentTablets").add("CloningTablets").add("BadTablets")
            .add("CompactionTooSlowTablets").add("OversizeTablets")
            .build();
    private static final Joiner JOINER = Joiner.on(",");

    final Database db;

    public IncompleteTabletsProcNode(Database db) {
        this.db = db;
    }

    @Override
    public ProcResult fetchResult() throws AnalysisException {
        StatisticProcDir.DBStatistic statistic = new StatisticProcDir.DBStatistic(db);
        return new BaseProcResult(TITLE_NAMES, Collections.singletonList(Arrays.asList(
                JOINER.join(statistic.unhealthyTabletIds),
                JOINER.join(statistic.inconsistentTabletIds),
                JOINER.join(statistic.cloningTabletIds),
                JOINER.join(statistic.unrecoverableTabletIds),
                JOINER.join(statistic.compactionTooSlowTabletIds),
                JOINER.join(statistic.oversizeTabletIds)
        )));
    }

}
