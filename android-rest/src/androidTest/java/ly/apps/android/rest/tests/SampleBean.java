/*
 * Copyright (C) 2013 47 Degrees, LLC
 * http://47deg.com
 * http://apps.ly
 * hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ly.apps.android.rest.tests;


import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Arrays;
import java.util.List;

public class SampleBean {

    @JsonProperty("_id")
    private String id;

    private String someString;

    private int someInt;

    private boolean someBoolean;

    private double someDouble;

    private List<String> stringList;

    private List<SampleNestedBean> beanList;

    private SampleNestedBean nestedBean;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSomeString() {
        return someString;
    }

    public void setSomeString(String someString) {
        this.someString = someString;
    }

    public int getSomeInt() {
        return someInt;
    }

    public void setSomeInt(int someInt) {
        this.someInt = someInt;
    }

    public boolean isSomeBoolean() {
        return someBoolean;
    }

    public void setSomeBoolean(boolean someBoolean) {
        this.someBoolean = someBoolean;
    }

    public double getSomeDouble() {
        return someDouble;
    }

    public void setSomeDouble(double someDouble) {
        this.someDouble = someDouble;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<SampleNestedBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<SampleNestedBean> beanList) {
        this.beanList = beanList;
    }

    public SampleNestedBean getNestedBean() {
        return nestedBean;
    }

    public void setNestedBean(SampleNestedBean nestedBean) {
        this.nestedBean = nestedBean;
    }

    public static SampleBean testInstance() {
        SampleBean sampleBean = new SampleBean();
        sampleBean.setId("id");
        sampleBean.setSomeBoolean(true);
        sampleBean.setSomeDouble(1.0);
        sampleBean.setSomeInt(1);
        SampleNestedBean nested = new SampleNestedBean();
        nested.setSomeString("someString");
        sampleBean.setNestedBean(nested);
        sampleBean.setStringList(Arrays.asList(nested.getSomeString()));
        sampleBean.setBeanList(Arrays.asList(nested));
        return sampleBean;
    }

    public static String testInstanceAsString() {
        return "{\"beanList\":[{\"someString\":\"someString\"}],\"nestedBean\":{\"someString\":\"someString\"},\"stringList\":[\"someString\"],\"someString\":null,\"someInt\":1,\"someDouble\":1.0,\"someBoolean\":true,\"_id\":\"id\"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SampleBean that = (SampleBean) o;

        if (someBoolean != that.someBoolean) return false;
        if (Double.compare(that.someDouble, someDouble) != 0) return false;
        if (someInt != that.someInt) return false;
        if (beanList != null ? !beanList.equals(that.beanList) : that.beanList != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (nestedBean != null ? !nestedBean.equals(that.nestedBean) : that.nestedBean != null) return false;
        if (someString != null ? !someString.equals(that.someString) : that.someString != null) return false;
        if (stringList != null ? !stringList.equals(that.stringList) : that.stringList != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (someString != null ? someString.hashCode() : 0);
        result = 31 * result + someInt;
        result = 31 * result + (someBoolean ? 1 : 0);
        temp = Double.doubleToLongBits(someDouble);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (stringList != null ? stringList.hashCode() : 0);
        result = 31 * result + (beanList != null ? beanList.hashCode() : 0);
        result = 31 * result + (nestedBean != null ? nestedBean.hashCode() : 0);
        return result;
    }
}
