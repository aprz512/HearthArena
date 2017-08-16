package com.aprz.heartharena.image;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.aprz.heartharena.image.freso.FrescoConstant;
import com.aprz.heartharena.image.freso.FrescoUtil;
import com.aprz.heartharena.image.freso.FrescoImageLoadConfig;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * Created by aprz on 17-8-7.
 * email: lyldalek@gmail.com
 * desc:
 */

public class ImageLoader extends BaseImageLoader<SimpleDraweeView, ImageInfo> {

    private final FrescoImageLoadConfig mDefaultConfig;

    private static class SingletonHolder {
        private static final ImageLoader INSTANCE = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private ImageLoader() {
        mDefaultConfig = new FrescoImageLoadConfig.Builder().create();
    }

    @Override
    public void load(SimpleDraweeView imageView, String url) {
        load(imageView, Uri.parse(url));
    }

    @Override
    public void load(SimpleDraweeView imageView, Uri uri) {
        load(imageView, uri, null);
    }

    @Override
    public void load(SimpleDraweeView imageView, Uri uri, ControllerListener<ImageInfo> listener) {
        try {
            FrescoUtil.checkViewNotNull(imageView);
            FrescoUtil.checkUriIsLegal(uri);
        } catch (Exception e) {
            uri = Uri.parse("asset://exception.png");
        }
        display(imageView, uri, listener, null);
    }

    private void display(SimpleDraweeView view, Uri uri, ControllerListener<ImageInfo> listener,
                         FrescoImageLoadConfig config) {

        if (!Fresco.hasBeenInitialized()) {
            return;
        }

        if (config == null) {
            config = mDefaultConfig;
        }
        GenericDraweeHierarchyBuilder hierarchyBuilder = new GenericDraweeHierarchyBuilder(null);
        hierarchyBuilder.setFadeDuration(config.fadeDuration);
        hierarchyBuilder.setRoundingParams(config.roundingParams);
        hierarchyBuilder.setActualImageScaleType(config.scaleType == null ? FrescoConstant.DEFAULT_SCALE_TYPE : config.scaleType);

        if (config.loadingDrawable != null) {
            ScalingUtils.ScaleType type = getScaleType(config.loadingDrawableScaleType, config);
            hierarchyBuilder.setPlaceholderImage(config.loadingDrawable, type);
        }
        if (config.failureDrawable != null) {
            ScalingUtils.ScaleType type = getScaleType(config.failureDrawableScaleType, config);
            hierarchyBuilder.setFailureImage(config.failureDrawable, type);
        }
        if (config.pressedDrawable != null) {
            hierarchyBuilder.setPressedStateOverlay(config.pressedDrawable);
        }
        if (config.retryDrawable != null) {
            hierarchyBuilder.setRetryImage(config.retryDrawable);
        }
        if (config.overlayDrawable != null) {
            hierarchyBuilder.setOverlay(config.overlayDrawable);
        }
        if (config.progressDrawable != null) {
            hierarchyBuilder.setProgressBarImage(config.progressDrawable);
        }

        if (config.aspectRatio > 0) {
            view.setAspectRatio(config.aspectRatio);
        }

        GenericDraweeHierarchy hierarchy = hierarchyBuilder.build();

        ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        if (config.autoRotate) {
            requestBuilder.setRotationOptions(RotationOptions.autoRotate());
        } else {
            requestBuilder.setRotationOptions(RotationOptions.disableRotation());
        }

        if (config.postprocessor != null) {
            requestBuilder.setPostprocessor(config.postprocessor);
        }

        //************************************按需压缩解码图片尺寸*******************************************
        int resizeWidth;
        int resizeHeight;
        if (config.resizeWidth > 0 && config.resizeHeight > 0) {
            resizeWidth = config.resizeWidth;
            resizeHeight = config.resizeHeight;
        } else {
            FrescoUtil.ImageViewSize size = FrescoUtil.getImageViewSize(view);
            resizeWidth = size.width;
            resizeHeight = size.height;
        }
        if (resizeWidth > 0 && resizeHeight > 0) {
            requestBuilder.setResizeOptions(new ResizeOptions(resizeWidth, resizeHeight));
        }
        requestBuilder.setImageDecodeOptions(ImageDecodeOptions.newBuilder().setDecodePreviewFrame(true).build());
        //************************************************************************************************

        ImageRequest imageRequest = requestBuilder.build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(listener)
                .setOldController(view.getController())
                .setAutoPlayAnimations(config.autoPlayAnimations)
                .setTapToRetryEnabled(config.tapToRetry)
                .setImageRequest(imageRequest)
                .build();

        view.setHierarchy(hierarchy);
        view.setController(controller);
    }

    private ScalingUtils.ScaleType getScaleType(ScalingUtils.ScaleType defaultType, FrescoImageLoadConfig config) {
        ScalingUtils.ScaleType type;
        if (defaultType == null) {
            type = config.scaleType;
            if (type == null) {
                type = FrescoConstant.DEFAULT_SCALE_TYPE;
            }
        } else {
            type = defaultType;
        }
        return type;
    }

    /**
     * 直接加载到本地disk
     */
    public static void prefetchToDiskCache(Uri uri) {
        if (uri == null) {
            return;
        }
        Fresco.getImagePipeline().prefetchToDiskCache(ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setProgressiveRenderingEnabled(false)
                .build(), null);
    }

    /**
     * 是否已加载到本地disk
     */
    public boolean isInDiskStorageCache(Uri uri) {
        if (uri == null) {
            return false;
        }
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, null);
        return ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey);
    }

    /**
     * 通过uri拿到本地sd卡上的cnt图片文件路径
     *
     */
    public String getDiskStorageCachePath(Uri uri) {
        return getDiskStorageCache(uri) == null ? null : getDiskStorageCache(uri).getAbsolutePath();
    }


    /**
     * 通过uri拿到本地sd卡上的cnt图片文件
     *
     */
    public File getDiskStorageCache(Uri uri) {
        if (!isInDiskStorageCache(uri)) {
            return null;
        }
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, null);
        BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
        return ((FileBinaryResource) resource).getFile();
    }

    public static void init(Context context) {
        context = !(context instanceof Application) ? context.getApplicationContext() : context;

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryName(FrescoConstant.DEFAULT_DISK_CACHE_DIR_NAME)
                .setBaseDirectoryPath(new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "Hearth Arena"))
                .setMaxCacheSize(FrescoConstant.DEFAULT_MAX_DISK_CACHE_SIZE)
                .setMaxCacheSizeOnLowDiskSpace(FrescoConstant.DEFAULT_LOW_SPACE_DISK_CACHE_SIZE)
                .setMaxCacheSizeOnVeryLowDiskSpace(FrescoConstant.DEFAULT_VERY_LOW_SPACE_DISK_CACHE_SIZE)
                .build();

        ImagePipelineConfig pipelineConfig = ImagePipelineConfig.newBuilder(context)
                .setBitmapsConfig(FrescoConstant.DEFAULT_BITMAP_CONFIG)
                .setDownsampleEnabled(true)
                .setResizeAndRotateEnabledForNetwork(true)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setResizeAndRotateEnabledForNetwork(true)
                .build();

        Fresco.initialize(context, pipelineConfig);
    }
}
