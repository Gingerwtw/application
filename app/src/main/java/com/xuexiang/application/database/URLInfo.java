/*
 * Copyright (C) 2022 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.application.database;

public class URLInfo {

    public long rowid;
    public String User_URL;
    public String usage;   // 0表示面象采集设备地址，1表示面像分析设备地址，2表示舌像分析地址，3表示舌象分析地址

    public URLInfo() {
        rowid = 0L;
        User_URL = "";
        usage = "";
    }
}
