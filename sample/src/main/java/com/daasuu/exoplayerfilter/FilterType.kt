package com.daasuu.exoplayerfilter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.daasuu.epf.filter.GlBilateralFilter
import com.daasuu.epf.filter.GlBoxBlurFilter
import com.daasuu.epf.filter.GlBrightnessFilter
import com.daasuu.epf.filter.GlBulgeDistortionFilter
import com.daasuu.epf.filter.GlCGAColorspaceFilter
import com.daasuu.epf.filter.GlContrastFilter
import com.daasuu.epf.filter.GlCrosshatchFilter
import com.daasuu.epf.filter.GlExposureFilter
import com.daasuu.epf.filter.GlFilter
import com.daasuu.epf.filter.GlFilterGroup
import com.daasuu.epf.filter.GlGammaFilter
import com.daasuu.epf.filter.GlGaussianBlurFilter
import com.daasuu.epf.filter.GlGrayScaleFilter
import com.daasuu.epf.filter.GlHalftoneFilter
import com.daasuu.epf.filter.GlHazeFilter
import com.daasuu.epf.filter.GlHighlightShadowFilter
import com.daasuu.epf.filter.GlHueFilter
import com.daasuu.epf.filter.GlInvertFilter
import com.daasuu.epf.filter.GlLookUpTableFilter
import com.daasuu.epf.filter.GlLuminanceFilter
import com.daasuu.epf.filter.GlLuminanceThresholdFilter
import com.daasuu.epf.filter.GlMonochromeFilter
import com.daasuu.epf.filter.GlOpacityFilter
import com.daasuu.epf.filter.GlPixelationFilter
import com.daasuu.epf.filter.GlPosterizeFilter
import com.daasuu.epf.filter.GlRGBFilter
import com.daasuu.epf.filter.GlSaturationFilter
import com.daasuu.epf.filter.GlSepiaFilter
import com.daasuu.epf.filter.GlSharpenFilter
import com.daasuu.epf.filter.GlSolarizeFilter
import com.daasuu.epf.filter.GlSphereRefractionFilter
import com.daasuu.epf.filter.GlSwirlFilter
import com.daasuu.epf.filter.GlToneCurveFilter
import com.daasuu.epf.filter.GlToneFilter
import com.daasuu.epf.filter.GlVibranceFilter
import com.daasuu.epf.filter.GlVignetteFilter
import com.daasuu.epf.filter.GlWatermarkFilter
import com.daasuu.epf.filter.GlWeakPixelInclusionFilter
import com.daasuu.epf.filter.GlWhiteBalanceFilter
import com.daasuu.epf.filter.GlZoomBlurFilter
import com.daasuu.exoplayerfilter.filtersample.GlBitmapOverlaySample
import java.io.IOException
import java.util.Arrays

/**
 * Created by sudamasayuki on 2017/05/18.
 */
enum class FilterType {
    DEFAULT,
    BITMAP_OVERLAY_SAMPLE,
    BILATERAL_BLUR,
    BOX_BLUR,
    BRIGHTNESS,
    BULGE_DISTORTION,
    CGA_COLORSPACE,
    CONTRAST,
    CROSSHATCH,
    EXPOSURE,
    FILTER_GROUP_SAMPLE,
    GAMMA,
    GAUSSIAN_FILTER,
    GRAY_SCALE,
    HAZE,
    HALFTONE,
    HIGHLIGHT_SHADOW,
    HUE,
    INVERT,
    LUMINANCE,
    LUMINANCE_THRESHOLD,
    MONOCHROME,
    OPACITY,
    OVERLAY,
    PIXELATION,
    POSTERIZE,
    RGB,
    SATURATION,
    SEPIA,
    SHARP,
    SOLARIZE,
    SPHERE_REFRACTION,
    SWIRL,
    TONE_CURVE_SAMPLE,
    TONE,
    VIBRANCE,
    VIGNETTE,
    LOOK_UP_TABLE_SAMPLE,
    WATERMARK,
    WEAK_PIXEL,
    WHITE_BALANCE,
    ZOOM_BLUR;

    companion object {
        fun createFilterList(): List<FilterType> {
            return Arrays.asList(*entries.toTypedArray())
        }

        //    public static GlFilter createGlFilter(FilterType filterType, Context context) {
        //        switch (filterType) {
        //            case DEFAULT:
        //                return new GlFilter();
        //            case SEPIA:
        //                return new GlSepiaFilter();
        //            case GRAY_SCALE:
        //                return new GlGrayScaleFilter();
        //            case INVERT:
        //                return new GlInvertFilter();
        //            case HAZE:
        //                return new GlHazeFilter();
        //            case MONOCHROME:
        //                return new GlMonochromeFilter();
        //            case BILATERAL_BLUR:
        //                return new GlBilateralFilter();
        //            case BOX_BLUR:
        //                return new GlBoxBlurFilter();
        //            case LOOK_UP_TABLE_SAMPLE:
        //                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lookup_sample);
        //
        //                return new GlLookUpTableFilter(bitmap);
        //            case TONE_CURVE_SAMPLE:
        //                try {
        //                    InputStream is = context.getAssets().open("acv/tone_cuver_sample.acv");
        //                    return new GlToneCurveFilter(is);
        //                } catch (IOException e) {
        //                    Log.e("FilterType", "Error");
        //                }
        //                return new GlFilter();
        //
        //            case SPHERE_REFRACTION:
        //                return new GlSphereRefractionFilter();
        //            case VIGNETTE:
        //                return new GlVignetteFilter();
        //            case FILTER_GROUP_SAMPLE:
        //                return new GlFilterGroup(new GlSepiaFilter(), new GlVignetteFilter());
        //            case GAUSSIAN_FILTER:
        //                return new GlGaussianBlurFilter();
        //            case BULGE_DISTORTION:
        //                return new GlBulgeDistortionFilter();
        //            case CGA_COLORSPACE:
        //                return new GlCGAColorspaceFilter();
        //            case SHARP:
        //                GlSharpenFilter glSharpenFilter = new GlSharpenFilter();
        //                glSharpenFilter.setSharpness(4f);
        //                return glSharpenFilter;
        //            case BITMAP_OVERLAY_SAMPLE:
        //                return new GlBitmapOverlaySample(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round));
        //            default:
        //                return new GlFilter();
        //        }
        //    }
        fun createGlFilter(filterType: FilterType?, context: Context): GlFilter {
            return when (filterType) {
                DEFAULT -> GlFilter()
                BILATERAL_BLUR -> GlBilateralFilter()
                BOX_BLUR -> GlBoxBlurFilter()
                BRIGHTNESS -> {
                    val glBrightnessFilter = GlBrightnessFilter()
                    glBrightnessFilter.setBrightness(0.2f)
                    glBrightnessFilter
                }

                BULGE_DISTORTION -> GlBulgeDistortionFilter()
                CGA_COLORSPACE -> GlCGAColorspaceFilter()
                CONTRAST -> {
                    val glContrastFilter = GlContrastFilter()
                    glContrastFilter.setContrast(2.5f)
                    glContrastFilter
                }

                CROSSHATCH -> GlCrosshatchFilter()
                EXPOSURE -> GlExposureFilter()
                FILTER_GROUP_SAMPLE -> GlFilterGroup(GlSepiaFilter(), GlVignetteFilter())
                GAMMA -> {
                    val glGammaFilter = GlGammaFilter()
                    glGammaFilter.setGamma(2f)
                    glGammaFilter
                }

                GAUSSIAN_FILTER -> GlGaussianBlurFilter()
                GRAY_SCALE -> GlGrayScaleFilter()
                HALFTONE -> GlHalftoneFilter()
                HAZE -> {
                    val glHazeFilter = GlHazeFilter()
                    glHazeFilter.slope = -0.5f
                    glHazeFilter
                }

                HIGHLIGHT_SHADOW -> GlHighlightShadowFilter()
                HUE -> GlHueFilter()
                INVERT -> GlInvertFilter()
                LOOK_UP_TABLE_SAMPLE -> {
                    val bitmap =
                        BitmapFactory.decodeResource(context.resources, R.drawable.lookup_sample)
                    GlLookUpTableFilter(bitmap)
                }

                LUMINANCE -> GlLuminanceFilter()
                LUMINANCE_THRESHOLD -> GlLuminanceThresholdFilter()
                MONOCHROME -> GlMonochromeFilter()
                OPACITY -> GlOpacityFilter()
                PIXELATION -> GlPixelationFilter()
                POSTERIZE -> GlPosterizeFilter()
                RGB -> {
                    val glRGBFilter = GlRGBFilter()
                    glRGBFilter.setRed(0f)
                    glRGBFilter
                }

                SATURATION -> GlSaturationFilter()
                SEPIA -> GlSepiaFilter()
                SHARP -> {
                    val glSharpenFilter = GlSharpenFilter()
                    glSharpenFilter.sharpness = 4f
                    glSharpenFilter
                }

                SOLARIZE -> GlSolarizeFilter()
                SPHERE_REFRACTION -> GlSphereRefractionFilter()
                SWIRL -> GlSwirlFilter()
                TONE_CURVE_SAMPLE -> {
                    try {
                        val `is` = context.assets.open("acv/tone_cuver_sample.acv")
                        return GlToneCurveFilter(`is`)
                    } catch (e: IOException) {
                        Log.e("FilterType", "Error")
                    }
                    GlFilter()
                }

                TONE -> GlToneFilter()
                VIBRANCE -> {
                    val glVibranceFilter = GlVibranceFilter()
                    glVibranceFilter.setVibrance(3f)
                    glVibranceFilter
                }

                VIGNETTE -> GlVignetteFilter()
                WATERMARK -> GlWatermarkFilter(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher_round
                    ), GlWatermarkFilter.Position.RIGHT_BOTTOM
                )

                WEAK_PIXEL -> GlWeakPixelInclusionFilter()
                WHITE_BALANCE -> {
                    val glWhiteBalanceFilter = GlWhiteBalanceFilter()
                    glWhiteBalanceFilter.setTemperature(2400f)
                    glWhiteBalanceFilter.setTint(2f)
                    glWhiteBalanceFilter
                }

                ZOOM_BLUR -> GlZoomBlurFilter()
                BITMAP_OVERLAY_SAMPLE -> GlBitmapOverlaySample(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher_round
                    )
                )

                else -> GlFilter()
            }
        }
    }
}
