package com.aprz.heartharena.image;

import android.net.Uri;
import android.widget.ImageView;

import com.facebook.drawee.controller.ControllerListener;

/**
 * Created by aprz on 17-8-7.
 * email: lyldalek@gmail.com
 * desc:
 */

public abstract class BaseImageLoader<T extends ImageView, E> {

    public abstract void load(T imageView, String url);

    public abstract void load(T imageView, Uri uri);

    public abstract void load(T imageView, Uri uri, ControllerListener<E> listener);

}
