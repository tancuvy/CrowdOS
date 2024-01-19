/**
 * Copyright (c) 2020-2024 CrowdOS_Group
 *
 * This file is part of CrowdOS and is licensed under
 * the terms of the Apache License 2.0, as found in the LICENSE file.
 */
package cn.crowdos.kernel.wrapper;

import cn.crowdos.kernel.constraint.Condition;

import javax.activation.DataContentHandler;
import java.util.Date;

public class DateCondition extends Date implements Condition {
    public DateCondition(){
        super();
    }
    public DateCondition(long date){
        super(date);
    }

    public DateCondition(String date){
        super(date);
    }

}
