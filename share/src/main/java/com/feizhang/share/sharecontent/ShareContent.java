package com.feizhang.share.sharecontent;

import android.content.Context;

import java.io.Serializable;

public abstract class ShareContent implements Serializable{

    public abstract boolean validate(Context context);
}
