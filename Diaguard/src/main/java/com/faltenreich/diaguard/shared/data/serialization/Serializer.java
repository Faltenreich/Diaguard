package com.faltenreich.diaguard.shared.data.serialization;

import androidx.annotation.Nullable;

import java.io.Serializable;

public interface Serializer<Input, Output extends Serializable> {
    @Nullable Output serialize(Input input);
    @Nullable Input deserialize(Output output);
}
