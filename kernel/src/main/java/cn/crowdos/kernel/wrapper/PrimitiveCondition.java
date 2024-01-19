/**
 * Copyright (c) 2020-2024 CrowdOS_Group
 *
 * This file is part of CrowdOS and is licensed under
 * the terms of the Apache License 2.0, as found in the LICENSE file.
 */
package cn.crowdos.kernel.wrapper;

import cn.crowdos.kernel.constraint.Condition;

public abstract class PrimitiveCondition<T> implements Condition {
    public final T primitive;

    protected PrimitiveCondition(T primitive) {
        this.primitive = primitive;
    }
}
