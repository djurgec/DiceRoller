package androidx.exifinterface.media;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.util.Pair;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class ExifInterface {
  public static final short ALTITUDE_ABOVE_SEA_LEVEL = 0;
  
  public static final short ALTITUDE_BELOW_SEA_LEVEL = 1;
  
  static final Charset ASCII;
  
  public static final int[] BITS_PER_SAMPLE_GREYSCALE_1;
  
  public static final int[] BITS_PER_SAMPLE_GREYSCALE_2;
  
  public static final int[] BITS_PER_SAMPLE_RGB;
  
  static final short BYTE_ALIGN_II = 18761;
  
  static final short BYTE_ALIGN_MM = 19789;
  
  public static final int COLOR_SPACE_S_RGB = 1;
  
  public static final int COLOR_SPACE_UNCALIBRATED = 65535;
  
  public static final short CONTRAST_HARD = 2;
  
  public static final short CONTRAST_NORMAL = 0;
  
  public static final short CONTRAST_SOFT = 1;
  
  public static final int DATA_DEFLATE_ZIP = 8;
  
  public static final int DATA_HUFFMAN_COMPRESSED = 2;
  
  public static final int DATA_JPEG = 6;
  
  public static final int DATA_JPEG_COMPRESSED = 7;
  
  public static final int DATA_LOSSY_JPEG = 34892;
  
  public static final int DATA_PACK_BITS_COMPRESSED = 32773;
  
  public static final int DATA_UNCOMPRESSED = 1;
  
  private static final boolean DEBUG = Log.isLoggable("ExifInterface", 3);
  
  static final byte[] EXIF_ASCII_PREFIX;
  
  private static final ExifTag[] EXIF_POINTER_TAGS;
  
  static final ExifTag[][] EXIF_TAGS;
  
  public static final short EXPOSURE_MODE_AUTO = 0;
  
  public static final short EXPOSURE_MODE_AUTO_BRACKET = 2;
  
  public static final short EXPOSURE_MODE_MANUAL = 1;
  
  public static final short EXPOSURE_PROGRAM_ACTION = 6;
  
  public static final short EXPOSURE_PROGRAM_APERTURE_PRIORITY = 3;
  
  public static final short EXPOSURE_PROGRAM_CREATIVE = 5;
  
  public static final short EXPOSURE_PROGRAM_LANDSCAPE_MODE = 8;
  
  public static final short EXPOSURE_PROGRAM_MANUAL = 1;
  
  public static final short EXPOSURE_PROGRAM_NORMAL = 2;
  
  public static final short EXPOSURE_PROGRAM_NOT_DEFINED = 0;
  
  public static final short EXPOSURE_PROGRAM_PORTRAIT_MODE = 7;
  
  public static final short EXPOSURE_PROGRAM_SHUTTER_PRIORITY = 4;
  
  public static final short FILE_SOURCE_DSC = 3;
  
  public static final short FILE_SOURCE_OTHER = 0;
  
  public static final short FILE_SOURCE_REFLEX_SCANNER = 2;
  
  public static final short FILE_SOURCE_TRANSPARENT_SCANNER = 1;
  
  public static final short FLAG_FLASH_FIRED = 1;
  
  public static final short FLAG_FLASH_MODE_AUTO = 24;
  
  public static final short FLAG_FLASH_MODE_COMPULSORY_FIRING = 8;
  
  public static final short FLAG_FLASH_MODE_COMPULSORY_SUPPRESSION = 16;
  
  public static final short FLAG_FLASH_NO_FLASH_FUNCTION = 32;
  
  public static final short FLAG_FLASH_RED_EYE_SUPPORTED = 64;
  
  public static final short FLAG_FLASH_RETURN_LIGHT_DETECTED = 6;
  
  public static final short FLAG_FLASH_RETURN_LIGHT_NOT_DETECTED = 4;
  
  private static final List<Integer> FLIPPED_ROTATION_ORDER;
  
  public static final short FORMAT_CHUNKY = 1;
  
  public static final short FORMAT_PLANAR = 2;
  
  public static final short GAIN_CONTROL_HIGH_GAIN_DOWN = 4;
  
  public static final short GAIN_CONTROL_HIGH_GAIN_UP = 2;
  
  public static final short GAIN_CONTROL_LOW_GAIN_DOWN = 3;
  
  public static final short GAIN_CONTROL_LOW_GAIN_UP = 1;
  
  public static final short GAIN_CONTROL_NONE = 0;
  
  public static final String GPS_DIRECTION_MAGNETIC = "M";
  
  public static final String GPS_DIRECTION_TRUE = "T";
  
  public static final String GPS_DISTANCE_KILOMETERS = "K";
  
  public static final String GPS_DISTANCE_MILES = "M";
  
  public static final String GPS_DISTANCE_NAUTICAL_MILES = "N";
  
  public static final String GPS_MEASUREMENT_2D = "2";
  
  public static final String GPS_MEASUREMENT_3D = "3";
  
  public static final short GPS_MEASUREMENT_DIFFERENTIAL_CORRECTED = 1;
  
  public static final String GPS_MEASUREMENT_INTERRUPTED = "V";
  
  public static final String GPS_MEASUREMENT_IN_PROGRESS = "A";
  
  public static final short GPS_MEASUREMENT_NO_DIFFERENTIAL = 0;
  
  public static final String GPS_SPEED_KILOMETERS_PER_HOUR = "K";
  
  public static final String GPS_SPEED_KNOTS = "N";
  
  public static final String GPS_SPEED_MILES_PER_HOUR = "M";
  
  private static final byte[] HEIF_BRAND_HEIC;
  
  private static final byte[] HEIF_BRAND_MIF1;
  
  private static final byte[] HEIF_TYPE_FTYP;
  
  static final byte[] IDENTIFIER_EXIF_APP1;
  
  private static final byte[] IDENTIFIER_XMP_APP1;
  
  private static final ExifTag[] IFD_EXIF_TAGS;
  
  private static final int IFD_FORMAT_BYTE = 1;
  
  static final int[] IFD_FORMAT_BYTES_PER_FORMAT;
  
  private static final int IFD_FORMAT_DOUBLE = 12;
  
  private static final int IFD_FORMAT_IFD = 13;
  
  static final String[] IFD_FORMAT_NAMES;
  
  private static final int IFD_FORMAT_SBYTE = 6;
  
  private static final int IFD_FORMAT_SINGLE = 11;
  
  private static final int IFD_FORMAT_SLONG = 9;
  
  private static final int IFD_FORMAT_SRATIONAL = 10;
  
  private static final int IFD_FORMAT_SSHORT = 8;
  
  private static final int IFD_FORMAT_STRING = 2;
  
  private static final int IFD_FORMAT_ULONG = 4;
  
  private static final int IFD_FORMAT_UNDEFINED = 7;
  
  private static final int IFD_FORMAT_URATIONAL = 5;
  
  private static final int IFD_FORMAT_USHORT = 3;
  
  private static final ExifTag[] IFD_GPS_TAGS;
  
  private static final ExifTag[] IFD_INTEROPERABILITY_TAGS;
  
  private static final int IFD_OFFSET = 8;
  
  private static final ExifTag[] IFD_THUMBNAIL_TAGS;
  
  private static final ExifTag[] IFD_TIFF_TAGS;
  
  private static final int IFD_TYPE_EXIF = 1;
  
  private static final int IFD_TYPE_GPS = 2;
  
  private static final int IFD_TYPE_INTEROPERABILITY = 3;
  
  private static final int IFD_TYPE_ORF_CAMERA_SETTINGS = 7;
  
  private static final int IFD_TYPE_ORF_IMAGE_PROCESSING = 8;
  
  private static final int IFD_TYPE_ORF_MAKER_NOTE = 6;
  
  private static final int IFD_TYPE_PEF = 9;
  
  static final int IFD_TYPE_PREVIEW = 5;
  
  static final int IFD_TYPE_PRIMARY = 0;
  
  static final int IFD_TYPE_THUMBNAIL = 4;
  
  private static final int IMAGE_TYPE_ARW = 1;
  
  private static final int IMAGE_TYPE_CR2 = 2;
  
  private static final int IMAGE_TYPE_DNG = 3;
  
  private static final int IMAGE_TYPE_HEIF = 12;
  
  private static final int IMAGE_TYPE_JPEG = 4;
  
  private static final int IMAGE_TYPE_NEF = 5;
  
  private static final int IMAGE_TYPE_NRW = 6;
  
  private static final int IMAGE_TYPE_ORF = 7;
  
  private static final int IMAGE_TYPE_PEF = 8;
  
  private static final int IMAGE_TYPE_PNG = 13;
  
  private static final int IMAGE_TYPE_RAF = 9;
  
  private static final int IMAGE_TYPE_RW2 = 10;
  
  private static final int IMAGE_TYPE_SRW = 11;
  
  private static final int IMAGE_TYPE_UNKNOWN = 0;
  
  private static final int IMAGE_TYPE_WEBP = 14;
  
  private static final ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG;
  
  private static final ExifTag JPEG_INTERCHANGE_FORMAT_TAG;
  
  static final byte[] JPEG_SIGNATURE;
  
  public static final String LATITUDE_NORTH = "N";
  
  public static final String LATITUDE_SOUTH = "S";
  
  public static final short LIGHT_SOURCE_CLOUDY_WEATHER = 10;
  
  public static final short LIGHT_SOURCE_COOL_WHITE_FLUORESCENT = 14;
  
  public static final short LIGHT_SOURCE_D50 = 23;
  
  public static final short LIGHT_SOURCE_D55 = 20;
  
  public static final short LIGHT_SOURCE_D65 = 21;
  
  public static final short LIGHT_SOURCE_D75 = 22;
  
  public static final short LIGHT_SOURCE_DAYLIGHT = 1;
  
  public static final short LIGHT_SOURCE_DAYLIGHT_FLUORESCENT = 12;
  
  public static final short LIGHT_SOURCE_DAY_WHITE_FLUORESCENT = 13;
  
  public static final short LIGHT_SOURCE_FINE_WEATHER = 9;
  
  public static final short LIGHT_SOURCE_FLASH = 4;
  
  public static final short LIGHT_SOURCE_FLUORESCENT = 2;
  
  public static final short LIGHT_SOURCE_ISO_STUDIO_TUNGSTEN = 24;
  
  public static final short LIGHT_SOURCE_OTHER = 255;
  
  public static final short LIGHT_SOURCE_SHADE = 11;
  
  public static final short LIGHT_SOURCE_STANDARD_LIGHT_A = 17;
  
  public static final short LIGHT_SOURCE_STANDARD_LIGHT_B = 18;
  
  public static final short LIGHT_SOURCE_STANDARD_LIGHT_C = 19;
  
  public static final short LIGHT_SOURCE_TUNGSTEN = 3;
  
  public static final short LIGHT_SOURCE_UNKNOWN = 0;
  
  public static final short LIGHT_SOURCE_WARM_WHITE_FLUORESCENT = 16;
  
  public static final short LIGHT_SOURCE_WHITE_FLUORESCENT = 15;
  
  public static final String LONGITUDE_EAST = "E";
  
  public static final String LONGITUDE_WEST = "W";
  
  static final byte MARKER = -1;
  
  static final byte MARKER_APP1 = -31;
  
  private static final byte MARKER_COM = -2;
  
  static final byte MARKER_EOI = -39;
  
  private static final byte MARKER_SOF0 = -64;
  
  private static final byte MARKER_SOF1 = -63;
  
  private static final byte MARKER_SOF10 = -54;
  
  private static final byte MARKER_SOF11 = -53;
  
  private static final byte MARKER_SOF13 = -51;
  
  private static final byte MARKER_SOF14 = -50;
  
  private static final byte MARKER_SOF15 = -49;
  
  private static final byte MARKER_SOF2 = -62;
  
  private static final byte MARKER_SOF3 = -61;
  
  private static final byte MARKER_SOF5 = -59;
  
  private static final byte MARKER_SOF6 = -58;
  
  private static final byte MARKER_SOF7 = -57;
  
  private static final byte MARKER_SOF9 = -55;
  
  private static final byte MARKER_SOI = -40;
  
  private static final byte MARKER_SOS = -38;
  
  private static final int MAX_THUMBNAIL_SIZE = 512;
  
  public static final short METERING_MODE_AVERAGE = 1;
  
  public static final short METERING_MODE_CENTER_WEIGHT_AVERAGE = 2;
  
  public static final short METERING_MODE_MULTI_SPOT = 4;
  
  public static final short METERING_MODE_OTHER = 255;
  
  public static final short METERING_MODE_PARTIAL = 6;
  
  public static final short METERING_MODE_PATTERN = 5;
  
  public static final short METERING_MODE_SPOT = 3;
  
  public static final short METERING_MODE_UNKNOWN = 0;
  
  private static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS;
  
  private static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS;
  
  private static final byte[] ORF_MAKER_NOTE_HEADER_1;
  
  private static final int ORF_MAKER_NOTE_HEADER_1_SIZE = 8;
  
  private static final byte[] ORF_MAKER_NOTE_HEADER_2;
  
  private static final int ORF_MAKER_NOTE_HEADER_2_SIZE = 12;
  
  private static final ExifTag[] ORF_MAKER_NOTE_TAGS;
  
  private static final short ORF_SIGNATURE_1 = 20306;
  
  private static final short ORF_SIGNATURE_2 = 21330;
  
  public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
  
  public static final int ORIENTATION_FLIP_VERTICAL = 4;
  
  public static final int ORIENTATION_NORMAL = 1;
  
  public static final int ORIENTATION_ROTATE_180 = 3;
  
  public static final int ORIENTATION_ROTATE_270 = 8;
  
  public static final int ORIENTATION_ROTATE_90 = 6;
  
  public static final int ORIENTATION_TRANSPOSE = 5;
  
  public static final int ORIENTATION_TRANSVERSE = 7;
  
  public static final int ORIENTATION_UNDEFINED = 0;
  
  public static final int ORIGINAL_RESOLUTION_IMAGE = 0;
  
  private static final int PEF_MAKER_NOTE_SKIP_SIZE = 6;
  
  private static final String PEF_SIGNATURE = "PENTAX";
  
  private static final ExifTag[] PEF_TAGS;
  
  public static final int PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO = 1;
  
  public static final int PHOTOMETRIC_INTERPRETATION_RGB = 2;
  
  public static final int PHOTOMETRIC_INTERPRETATION_WHITE_IS_ZERO = 0;
  
  public static final int PHOTOMETRIC_INTERPRETATION_YCBCR = 6;
  
  private static final int PNG_CHUNK_CRC_BYTE_LENGTH = 4;
  
  private static final int PNG_CHUNK_TYPE_BYTE_LENGTH = 4;
  
  private static final byte[] PNG_CHUNK_TYPE_EXIF;
  
  private static final byte[] PNG_CHUNK_TYPE_IEND;
  
  private static final byte[] PNG_CHUNK_TYPE_IHDR;
  
  private static final byte[] PNG_SIGNATURE;
  
  private static final int RAF_JPEG_LENGTH_VALUE_SIZE = 4;
  
  private static final int RAF_OFFSET_TO_JPEG_IMAGE_OFFSET = 84;
  
  private static final String RAF_SIGNATURE = "FUJIFILMCCD-RAW";
  
  public static final int REDUCED_RESOLUTION_IMAGE = 1;
  
  public static final short RENDERED_PROCESS_CUSTOM = 1;
  
  public static final short RENDERED_PROCESS_NORMAL = 0;
  
  public static final short RESOLUTION_UNIT_CENTIMETERS = 3;
  
  public static final short RESOLUTION_UNIT_INCHES = 2;
  
  private static final List<Integer> ROTATION_ORDER;
  
  private static final short RW2_SIGNATURE = 85;
  
  public static final short SATURATION_HIGH = 0;
  
  public static final short SATURATION_LOW = 0;
  
  public static final short SATURATION_NORMAL = 0;
  
  public static final short SCENE_CAPTURE_TYPE_LANDSCAPE = 1;
  
  public static final short SCENE_CAPTURE_TYPE_NIGHT = 3;
  
  public static final short SCENE_CAPTURE_TYPE_PORTRAIT = 2;
  
  public static final short SCENE_CAPTURE_TYPE_STANDARD = 0;
  
  public static final short SCENE_TYPE_DIRECTLY_PHOTOGRAPHED = 1;
  
  public static final short SENSITIVITY_TYPE_ISO_SPEED = 3;
  
  public static final short SENSITIVITY_TYPE_REI = 2;
  
  public static final short SENSITIVITY_TYPE_REI_AND_ISO = 6;
  
  public static final short SENSITIVITY_TYPE_SOS = 1;
  
  public static final short SENSITIVITY_TYPE_SOS_AND_ISO = 5;
  
  public static final short SENSITIVITY_TYPE_SOS_AND_REI = 4;
  
  public static final short SENSITIVITY_TYPE_SOS_AND_REI_AND_ISO = 7;
  
  public static final short SENSITIVITY_TYPE_UNKNOWN = 0;
  
  public static final short SENSOR_TYPE_COLOR_SEQUENTIAL = 5;
  
  public static final short SENSOR_TYPE_COLOR_SEQUENTIAL_LINEAR = 8;
  
  public static final short SENSOR_TYPE_NOT_DEFINED = 1;
  
  public static final short SENSOR_TYPE_ONE_CHIP = 2;
  
  public static final short SENSOR_TYPE_THREE_CHIP = 4;
  
  public static final short SENSOR_TYPE_TRILINEAR = 7;
  
  public static final short SENSOR_TYPE_TWO_CHIP = 3;
  
  public static final short SHARPNESS_HARD = 2;
  
  public static final short SHARPNESS_NORMAL = 0;
  
  public static final short SHARPNESS_SOFT = 1;
  
  private static final int SIGNATURE_CHECK_SIZE = 5000;
  
  static final byte START_CODE = 42;
  
  public static final int STREAM_TYPE_EXIF_DATA_ONLY = 1;
  
  public static final int STREAM_TYPE_FULL_IMAGE_DATA = 0;
  
  public static final short SUBJECT_DISTANCE_RANGE_CLOSE_VIEW = 2;
  
  public static final short SUBJECT_DISTANCE_RANGE_DISTANT_VIEW = 3;
  
  public static final short SUBJECT_DISTANCE_RANGE_MACRO = 1;
  
  public static final short SUBJECT_DISTANCE_RANGE_UNKNOWN = 0;
  
  private static final String TAG = "ExifInterface";
  
  public static final String TAG_APERTURE_VALUE = "ApertureValue";
  
  public static final String TAG_ARTIST = "Artist";
  
  public static final String TAG_BITS_PER_SAMPLE = "BitsPerSample";
  
  public static final String TAG_BODY_SERIAL_NUMBER = "BodySerialNumber";
  
  public static final String TAG_BRIGHTNESS_VALUE = "BrightnessValue";
  
  @Deprecated
  public static final String TAG_CAMARA_OWNER_NAME = "CameraOwnerName";
  
  public static final String TAG_CAMERA_OWNER_NAME = "CameraOwnerName";
  
  public static final String TAG_CFA_PATTERN = "CFAPattern";
  
  public static final String TAG_COLOR_SPACE = "ColorSpace";
  
  public static final String TAG_COMPONENTS_CONFIGURATION = "ComponentsConfiguration";
  
  public static final String TAG_COMPRESSED_BITS_PER_PIXEL = "CompressedBitsPerPixel";
  
  public static final String TAG_COMPRESSION = "Compression";
  
  public static final String TAG_CONTRAST = "Contrast";
  
  public static final String TAG_COPYRIGHT = "Copyright";
  
  public static final String TAG_CUSTOM_RENDERED = "CustomRendered";
  
  public static final String TAG_DATETIME = "DateTime";
  
  public static final String TAG_DATETIME_DIGITIZED = "DateTimeDigitized";
  
  public static final String TAG_DATETIME_ORIGINAL = "DateTimeOriginal";
  
  public static final String TAG_DEFAULT_CROP_SIZE = "DefaultCropSize";
  
  public static final String TAG_DEVICE_SETTING_DESCRIPTION = "DeviceSettingDescription";
  
  public static final String TAG_DIGITAL_ZOOM_RATIO = "DigitalZoomRatio";
  
  public static final String TAG_DNG_VERSION = "DNGVersion";
  
  private static final String TAG_EXIF_IFD_POINTER = "ExifIFDPointer";
  
  public static final String TAG_EXIF_VERSION = "ExifVersion";
  
  public static final String TAG_EXPOSURE_BIAS_VALUE = "ExposureBiasValue";
  
  public static final String TAG_EXPOSURE_INDEX = "ExposureIndex";
  
  public static final String TAG_EXPOSURE_MODE = "ExposureMode";
  
  public static final String TAG_EXPOSURE_PROGRAM = "ExposureProgram";
  
  public static final String TAG_EXPOSURE_TIME = "ExposureTime";
  
  public static final String TAG_FILE_SOURCE = "FileSource";
  
  public static final String TAG_FLASH = "Flash";
  
  public static final String TAG_FLASHPIX_VERSION = "FlashpixVersion";
  
  public static final String TAG_FLASH_ENERGY = "FlashEnergy";
  
  public static final String TAG_FOCAL_LENGTH = "FocalLength";
  
  public static final String TAG_FOCAL_LENGTH_IN_35MM_FILM = "FocalLengthIn35mmFilm";
  
  public static final String TAG_FOCAL_PLANE_RESOLUTION_UNIT = "FocalPlaneResolutionUnit";
  
  public static final String TAG_FOCAL_PLANE_X_RESOLUTION = "FocalPlaneXResolution";
  
  public static final String TAG_FOCAL_PLANE_Y_RESOLUTION = "FocalPlaneYResolution";
  
  public static final String TAG_F_NUMBER = "FNumber";
  
  public static final String TAG_GAIN_CONTROL = "GainControl";
  
  public static final String TAG_GAMMA = "Gamma";
  
  public static final String TAG_GPS_ALTITUDE = "GPSAltitude";
  
  public static final String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";
  
  public static final String TAG_GPS_AREA_INFORMATION = "GPSAreaInformation";
  
  public static final String TAG_GPS_DATESTAMP = "GPSDateStamp";
  
  public static final String TAG_GPS_DEST_BEARING = "GPSDestBearing";
  
  public static final String TAG_GPS_DEST_BEARING_REF = "GPSDestBearingRef";
  
  public static final String TAG_GPS_DEST_DISTANCE = "GPSDestDistance";
  
  public static final String TAG_GPS_DEST_DISTANCE_REF = "GPSDestDistanceRef";
  
  public static final String TAG_GPS_DEST_LATITUDE = "GPSDestLatitude";
  
  public static final String TAG_GPS_DEST_LATITUDE_REF = "GPSDestLatitudeRef";
  
  public static final String TAG_GPS_DEST_LONGITUDE = "GPSDestLongitude";
  
  public static final String TAG_GPS_DEST_LONGITUDE_REF = "GPSDestLongitudeRef";
  
  public static final String TAG_GPS_DIFFERENTIAL = "GPSDifferential";
  
  public static final String TAG_GPS_DOP = "GPSDOP";
  
  public static final String TAG_GPS_H_POSITIONING_ERROR = "GPSHPositioningError";
  
  public static final String TAG_GPS_IMG_DIRECTION = "GPSImgDirection";
  
  public static final String TAG_GPS_IMG_DIRECTION_REF = "GPSImgDirectionRef";
  
  private static final String TAG_GPS_INFO_IFD_POINTER = "GPSInfoIFDPointer";
  
  public static final String TAG_GPS_LATITUDE = "GPSLatitude";
  
  public static final String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";
  
  public static final String TAG_GPS_LONGITUDE = "GPSLongitude";
  
  public static final String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";
  
  public static final String TAG_GPS_MAP_DATUM = "GPSMapDatum";
  
  public static final String TAG_GPS_MEASURE_MODE = "GPSMeasureMode";
  
  public static final String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";
  
  public static final String TAG_GPS_SATELLITES = "GPSSatellites";
  
  public static final String TAG_GPS_SPEED = "GPSSpeed";
  
  public static final String TAG_GPS_SPEED_REF = "GPSSpeedRef";
  
  public static final String TAG_GPS_STATUS = "GPSStatus";
  
  public static final String TAG_GPS_TIMESTAMP = "GPSTimeStamp";
  
  public static final String TAG_GPS_TRACK = "GPSTrack";
  
  public static final String TAG_GPS_TRACK_REF = "GPSTrackRef";
  
  public static final String TAG_GPS_VERSION_ID = "GPSVersionID";
  
  public static final String TAG_IMAGE_DESCRIPTION = "ImageDescription";
  
  public static final String TAG_IMAGE_LENGTH = "ImageLength";
  
  public static final String TAG_IMAGE_UNIQUE_ID = "ImageUniqueID";
  
  public static final String TAG_IMAGE_WIDTH = "ImageWidth";
  
  private static final String TAG_INTEROPERABILITY_IFD_POINTER = "InteroperabilityIFDPointer";
  
  public static final String TAG_INTEROPERABILITY_INDEX = "InteroperabilityIndex";
  
  public static final String TAG_ISO_SPEED = "ISOSpeed";
  
  public static final String TAG_ISO_SPEED_LATITUDE_YYY = "ISOSpeedLatitudeyyy";
  
  public static final String TAG_ISO_SPEED_LATITUDE_ZZZ = "ISOSpeedLatitudezzz";
  
  @Deprecated
  public static final String TAG_ISO_SPEED_RATINGS = "ISOSpeedRatings";
  
  public static final String TAG_JPEG_INTERCHANGE_FORMAT = "JPEGInterchangeFormat";
  
  public static final String TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = "JPEGInterchangeFormatLength";
  
  public static final String TAG_LENS_MAKE = "LensMake";
  
  public static final String TAG_LENS_MODEL = "LensModel";
  
  public static final String TAG_LENS_SERIAL_NUMBER = "LensSerialNumber";
  
  public static final String TAG_LENS_SPECIFICATION = "LensSpecification";
  
  public static final String TAG_LIGHT_SOURCE = "LightSource";
  
  public static final String TAG_MAKE = "Make";
  
  public static final String TAG_MAKER_NOTE = "MakerNote";
  
  public static final String TAG_MAX_APERTURE_VALUE = "MaxApertureValue";
  
  public static final String TAG_METERING_MODE = "MeteringMode";
  
  public static final String TAG_MODEL = "Model";
  
  public static final String TAG_NEW_SUBFILE_TYPE = "NewSubfileType";
  
  public static final String TAG_OECF = "OECF";
  
  public static final String TAG_OFFSET_TIME = "OffsetTime";
  
  public static final String TAG_OFFSET_TIME_DIGITIZED = "OffsetTimeDigitized";
  
  public static final String TAG_OFFSET_TIME_ORIGINAL = "OffsetTimeOriginal";
  
  public static final String TAG_ORF_ASPECT_FRAME = "AspectFrame";
  
  private static final String TAG_ORF_CAMERA_SETTINGS_IFD_POINTER = "CameraSettingsIFDPointer";
  
  private static final String TAG_ORF_IMAGE_PROCESSING_IFD_POINTER = "ImageProcessingIFDPointer";
  
  public static final String TAG_ORF_PREVIEW_IMAGE_LENGTH = "PreviewImageLength";
  
  public static final String TAG_ORF_PREVIEW_IMAGE_START = "PreviewImageStart";
  
  public static final String TAG_ORF_THUMBNAIL_IMAGE = "ThumbnailImage";
  
  public static final String TAG_ORIENTATION = "Orientation";
  
  public static final String TAG_PHOTOGRAPHIC_SENSITIVITY = "PhotographicSensitivity";
  
  public static final String TAG_PHOTOMETRIC_INTERPRETATION = "PhotometricInterpretation";
  
  public static final String TAG_PIXEL_X_DIMENSION = "PixelXDimension";
  
  public static final String TAG_PIXEL_Y_DIMENSION = "PixelYDimension";
  
  public static final String TAG_PLANAR_CONFIGURATION = "PlanarConfiguration";
  
  public static final String TAG_PRIMARY_CHROMATICITIES = "PrimaryChromaticities";
  
  private static final ExifTag TAG_RAF_IMAGE_SIZE;
  
  public static final String TAG_RECOMMENDED_EXPOSURE_INDEX = "RecommendedExposureIndex";
  
  public static final String TAG_REFERENCE_BLACK_WHITE = "ReferenceBlackWhite";
  
  public static final String TAG_RELATED_SOUND_FILE = "RelatedSoundFile";
  
  public static final String TAG_RESOLUTION_UNIT = "ResolutionUnit";
  
  public static final String TAG_ROWS_PER_STRIP = "RowsPerStrip";
  
  public static final String TAG_RW2_ISO = "ISO";
  
  public static final String TAG_RW2_JPG_FROM_RAW = "JpgFromRaw";
  
  public static final String TAG_RW2_SENSOR_BOTTOM_BORDER = "SensorBottomBorder";
  
  public static final String TAG_RW2_SENSOR_LEFT_BORDER = "SensorLeftBorder";
  
  public static final String TAG_RW2_SENSOR_RIGHT_BORDER = "SensorRightBorder";
  
  public static final String TAG_RW2_SENSOR_TOP_BORDER = "SensorTopBorder";
  
  public static final String TAG_SAMPLES_PER_PIXEL = "SamplesPerPixel";
  
  public static final String TAG_SATURATION = "Saturation";
  
  public static final String TAG_SCENE_CAPTURE_TYPE = "SceneCaptureType";
  
  public static final String TAG_SCENE_TYPE = "SceneType";
  
  public static final String TAG_SENSING_METHOD = "SensingMethod";
  
  public static final String TAG_SENSITIVITY_TYPE = "SensitivityType";
  
  public static final String TAG_SHARPNESS = "Sharpness";
  
  public static final String TAG_SHUTTER_SPEED_VALUE = "ShutterSpeedValue";
  
  public static final String TAG_SOFTWARE = "Software";
  
  public static final String TAG_SPATIAL_FREQUENCY_RESPONSE = "SpatialFrequencyResponse";
  
  public static final String TAG_SPECTRAL_SENSITIVITY = "SpectralSensitivity";
  
  public static final String TAG_STANDARD_OUTPUT_SENSITIVITY = "StandardOutputSensitivity";
  
  public static final String TAG_STRIP_BYTE_COUNTS = "StripByteCounts";
  
  public static final String TAG_STRIP_OFFSETS = "StripOffsets";
  
  public static final String TAG_SUBFILE_TYPE = "SubfileType";
  
  public static final String TAG_SUBJECT_AREA = "SubjectArea";
  
  public static final String TAG_SUBJECT_DISTANCE = "SubjectDistance";
  
  public static final String TAG_SUBJECT_DISTANCE_RANGE = "SubjectDistanceRange";
  
  public static final String TAG_SUBJECT_LOCATION = "SubjectLocation";
  
  public static final String TAG_SUBSEC_TIME = "SubSecTime";
  
  public static final String TAG_SUBSEC_TIME_DIGITIZED = "SubSecTimeDigitized";
  
  public static final String TAG_SUBSEC_TIME_ORIGINAL = "SubSecTimeOriginal";
  
  private static final String TAG_SUB_IFD_POINTER = "SubIFDPointer";
  
  public static final String TAG_THUMBNAIL_IMAGE_LENGTH = "ThumbnailImageLength";
  
  public static final String TAG_THUMBNAIL_IMAGE_WIDTH = "ThumbnailImageWidth";
  
  public static final String TAG_THUMBNAIL_ORIENTATION = "ThumbnailOrientation";
  
  public static final String TAG_TRANSFER_FUNCTION = "TransferFunction";
  
  public static final String TAG_USER_COMMENT = "UserComment";
  
  public static final String TAG_WHITE_BALANCE = "WhiteBalance";
  
  public static final String TAG_WHITE_POINT = "WhitePoint";
  
  public static final String TAG_XMP = "Xmp";
  
  public static final String TAG_X_RESOLUTION = "XResolution";
  
  public static final String TAG_Y_CB_CR_COEFFICIENTS = "YCbCrCoefficients";
  
  public static final String TAG_Y_CB_CR_POSITIONING = "YCbCrPositioning";
  
  public static final String TAG_Y_CB_CR_SUB_SAMPLING = "YCbCrSubSampling";
  
  public static final String TAG_Y_RESOLUTION = "YResolution";
  
  private static final int WEBP_CHUNK_SIZE_BYTE_LENGTH = 4;
  
  private static final byte[] WEBP_CHUNK_TYPE_ANIM;
  
  private static final byte[] WEBP_CHUNK_TYPE_ANMF;
  
  private static final int WEBP_CHUNK_TYPE_BYTE_LENGTH = 4;
  
  private static final byte[] WEBP_CHUNK_TYPE_EXIF;
  
  private static final byte[] WEBP_CHUNK_TYPE_VP8;
  
  private static final byte[] WEBP_CHUNK_TYPE_VP8L;
  
  private static final byte[] WEBP_CHUNK_TYPE_VP8X;
  
  private static final int WEBP_CHUNK_TYPE_VP8X_DEFAULT_LENGTH = 10;
  
  private static final byte[] WEBP_CHUNK_TYPE_XMP;
  
  private static final int WEBP_FILE_SIZE_BYTE_LENGTH = 4;
  
  private static final byte[] WEBP_SIGNATURE_1;
  
  private static final byte[] WEBP_SIGNATURE_2;
  
  private static final byte WEBP_VP8L_SIGNATURE = 47;
  
  private static final byte[] WEBP_VP8_SIGNATURE;
  
  @Deprecated
  public static final int WHITEBALANCE_AUTO = 0;
  
  @Deprecated
  public static final int WHITEBALANCE_MANUAL = 1;
  
  public static final short WHITE_BALANCE_AUTO = 0;
  
  public static final short WHITE_BALANCE_MANUAL = 1;
  
  public static final short Y_CB_CR_POSITIONING_CENTERED = 1;
  
  public static final short Y_CB_CR_POSITIONING_CO_SITED = 2;
  
  private static final HashMap<Integer, Integer> sExifPointerTagMap;
  
  private static final HashMap<Integer, ExifTag>[] sExifTagMapsForReading;
  
  private static final HashMap<String, ExifTag>[] sExifTagMapsForWriting;
  
  private static SimpleDateFormat sFormatter;
  
  private static final Pattern sGpsTimestampPattern;
  
  private static final Pattern sNonZeroTimePattern;
  
  private static final HashSet<String> sTagSetForCompatibility;
  
  private boolean mAreThumbnailStripsConsecutive;
  
  private AssetManager.AssetInputStream mAssetInputStream;
  
  private final HashMap<String, ExifAttribute>[] mAttributes;
  
  private Set<Integer> mAttributesOffsets;
  
  private ByteOrder mExifByteOrder;
  
  private int mExifOffset;
  
  private String mFilename;
  
  private boolean mHasThumbnail;
  
  private boolean mHasThumbnailStrips;
  
  private boolean mIsExifDataOnly;
  
  private boolean mIsSupportedFile;
  
  private int mMimeType;
  
  private boolean mModified;
  
  private int mOrfMakerNoteOffset;
  
  private int mOrfThumbnailLength;
  
  private int mOrfThumbnailOffset;
  
  private int mRw2JpgFromRawOffset;
  
  private FileDescriptor mSeekableFileDescriptor;
  
  private byte[] mThumbnailBytes;
  
  private int mThumbnailCompression;
  
  private int mThumbnailLength;
  
  private int mThumbnailOffset;
  
  private boolean mXmpIsFromSeparateMarker;
  
  static {
    Integer integer6 = Integer.valueOf(1);
    Integer integer2 = Integer.valueOf(2);
    Integer integer1 = Integer.valueOf(8);
    ROTATION_ORDER = Arrays.asList(new Integer[] { integer6, Integer.valueOf(6), integer4, integer1 });
    Integer integer5 = Integer.valueOf(7);
    Integer integer3 = Integer.valueOf(5);
    FLIPPED_ROTATION_ORDER = Arrays.asList(new Integer[] { integer2, integer5, Integer.valueOf(4), integer3 });
    BITS_PER_SAMPLE_RGB = new int[] { 8, 8, 8 };
    BITS_PER_SAMPLE_GREYSCALE_1 = new int[] { 4 };
    BITS_PER_SAMPLE_GREYSCALE_2 = new int[] { 8 };
    JPEG_SIGNATURE = new byte[] { -1, -40, -1 };
    HEIF_TYPE_FTYP = new byte[] { 102, 116, 121, 112 };
    HEIF_BRAND_MIF1 = new byte[] { 109, 105, 102, 49 };
    HEIF_BRAND_HEIC = new byte[] { 104, 101, 105, 99 };
    ORF_MAKER_NOTE_HEADER_1 = new byte[] { 79, 76, 89, 77, 80, 0 };
    ORF_MAKER_NOTE_HEADER_2 = new byte[] { 79, 76, 89, 77, 80, 85, 83, 0, 73, 73 };
    PNG_SIGNATURE = new byte[] { -119, 80, 78, 71, 13, 10, 26, 10 };
    PNG_CHUNK_TYPE_EXIF = new byte[] { 101, 88, 73, 102 };
    PNG_CHUNK_TYPE_IHDR = new byte[] { 73, 72, 68, 82 };
    PNG_CHUNK_TYPE_IEND = new byte[] { 73, 69, 78, 68 };
    WEBP_SIGNATURE_1 = new byte[] { 82, 73, 70, 70 };
    WEBP_SIGNATURE_2 = new byte[] { 87, 69, 66, 80 };
    WEBP_CHUNK_TYPE_EXIF = new byte[] { 69, 88, 73, 70 };
    WEBP_VP8_SIGNATURE = new byte[] { -99, 1, 42 };
    WEBP_CHUNK_TYPE_VP8X = "VP8X".getBytes(Charset.defaultCharset());
    WEBP_CHUNK_TYPE_VP8L = "VP8L".getBytes(Charset.defaultCharset());
    WEBP_CHUNK_TYPE_VP8 = "VP8 ".getBytes(Charset.defaultCharset());
    WEBP_CHUNK_TYPE_ANIM = "ANIM".getBytes(Charset.defaultCharset());
    WEBP_CHUNK_TYPE_ANMF = "ANMF".getBytes(Charset.defaultCharset());
    WEBP_CHUNK_TYPE_XMP = "XMP ".getBytes(Charset.defaultCharset());
    IFD_FORMAT_NAMES = new String[] { 
        "", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", 
        "SRATIONAL", "SINGLE", "DOUBLE", "IFD" };
    IFD_FORMAT_BYTES_PER_FORMAT = new int[] { 
        0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 
        8, 4, 8, 1 };
    EXIF_ASCII_PREFIX = new byte[] { 65, 83, 67, 73, 73, 0, 0, 0 };
    ExifTag[] arrayOfExifTag3 = new ExifTag[42];
    arrayOfExifTag3[0] = new ExifTag("NewSubfileType", 254, 4);
    arrayOfExifTag3[1] = new ExifTag("SubfileType", 255, 4);
    arrayOfExifTag3[2] = new ExifTag("ImageWidth", 256, 3, 4);
    arrayOfExifTag3[3] = new ExifTag("ImageLength", 257, 3, 4);
    arrayOfExifTag3[4] = new ExifTag("BitsPerSample", 258, 3);
    arrayOfExifTag3[5] = new ExifTag("Compression", 259, 3);
    arrayOfExifTag3[6] = new ExifTag("PhotometricInterpretation", 262, 3);
    arrayOfExifTag3[7] = new ExifTag("ImageDescription", 270, 2);
    arrayOfExifTag3[8] = new ExifTag("Make", 271, 2);
    arrayOfExifTag3[9] = new ExifTag("Model", 272, 2);
    arrayOfExifTag3[10] = new ExifTag("StripOffsets", 273, 3, 4);
    arrayOfExifTag3[11] = new ExifTag("Orientation", 274, 3);
    arrayOfExifTag3[12] = new ExifTag("SamplesPerPixel", 277, 3);
    arrayOfExifTag3[13] = new ExifTag("RowsPerStrip", 278, 3, 4);
    arrayOfExifTag3[14] = new ExifTag("StripByteCounts", 279, 3, 4);
    arrayOfExifTag3[15] = new ExifTag("XResolution", 282, 5);
    arrayOfExifTag3[16] = new ExifTag("YResolution", 283, 5);
    arrayOfExifTag3[17] = new ExifTag("PlanarConfiguration", 284, 3);
    arrayOfExifTag3[18] = new ExifTag("ResolutionUnit", 296, 3);
    arrayOfExifTag3[19] = new ExifTag("TransferFunction", 301, 3);
    arrayOfExifTag3[20] = new ExifTag("Software", 305, 2);
    arrayOfExifTag3[21] = new ExifTag("DateTime", 306, 2);
    arrayOfExifTag3[22] = new ExifTag("Artist", 315, 2);
    arrayOfExifTag3[23] = new ExifTag("WhitePoint", 318, 5);
    arrayOfExifTag3[24] = new ExifTag("PrimaryChromaticities", 319, 5);
    arrayOfExifTag3[25] = new ExifTag("SubIFDPointer", 330, 4);
    arrayOfExifTag3[26] = new ExifTag("JPEGInterchangeFormat", 513, 4);
    arrayOfExifTag3[27] = new ExifTag("JPEGInterchangeFormatLength", 514, 4);
    arrayOfExifTag3[28] = new ExifTag("YCbCrCoefficients", 529, 5);
    arrayOfExifTag3[29] = new ExifTag("YCbCrSubSampling", 530, 3);
    arrayOfExifTag3[30] = new ExifTag("YCbCrPositioning", 531, 3);
    arrayOfExifTag3[31] = new ExifTag("ReferenceBlackWhite", 532, 5);
    arrayOfExifTag3[32] = new ExifTag("Copyright", 33432, 2);
    arrayOfExifTag3[33] = new ExifTag("ExifIFDPointer", 34665, 4);
    arrayOfExifTag3[34] = new ExifTag("GPSInfoIFDPointer", 34853, 4);
    arrayOfExifTag3[35] = new ExifTag("SensorTopBorder", 4, 4);
    arrayOfExifTag3[36] = new ExifTag("SensorLeftBorder", 5, 4);
    arrayOfExifTag3[37] = new ExifTag("SensorBottomBorder", 6, 4);
    arrayOfExifTag3[38] = new ExifTag("SensorRightBorder", 7, 4);
    arrayOfExifTag3[39] = new ExifTag("ISO", 23, 3);
    arrayOfExifTag3[40] = new ExifTag("JpgFromRaw", 46, 7);
    arrayOfExifTag3[41] = new ExifTag("Xmp", 700, 1);
    IFD_TIFF_TAGS = arrayOfExifTag3;
    ExifTag[] arrayOfExifTag4 = new ExifTag[74];
    arrayOfExifTag4[0] = new ExifTag("ExposureTime", 33434, 5);
    arrayOfExifTag4[1] = new ExifTag("FNumber", 33437, 5);
    arrayOfExifTag4[2] = new ExifTag("ExposureProgram", 34850, 3);
    arrayOfExifTag4[3] = new ExifTag("SpectralSensitivity", 34852, 2);
    arrayOfExifTag4[4] = new ExifTag("PhotographicSensitivity", 34855, 3);
    arrayOfExifTag4[5] = new ExifTag("OECF", 34856, 7);
    arrayOfExifTag4[6] = new ExifTag("SensitivityType", 34864, 3);
    arrayOfExifTag4[7] = new ExifTag("StandardOutputSensitivity", 34865, 4);
    arrayOfExifTag4[8] = new ExifTag("RecommendedExposureIndex", 34866, 4);
    arrayOfExifTag4[9] = new ExifTag("ISOSpeed", 34867, 4);
    arrayOfExifTag4[10] = new ExifTag("ISOSpeedLatitudeyyy", 34868, 4);
    arrayOfExifTag4[11] = new ExifTag("ISOSpeedLatitudezzz", 34869, 4);
    arrayOfExifTag4[12] = new ExifTag("ExifVersion", 36864, 2);
    arrayOfExifTag4[13] = new ExifTag("DateTimeOriginal", 36867, 2);
    arrayOfExifTag4[14] = new ExifTag("DateTimeDigitized", 36868, 2);
    arrayOfExifTag4[15] = new ExifTag("OffsetTime", 36880, 2);
    arrayOfExifTag4[16] = new ExifTag("OffsetTimeOriginal", 36881, 2);
    arrayOfExifTag4[17] = new ExifTag("OffsetTimeDigitized", 36882, 2);
    arrayOfExifTag4[18] = new ExifTag("ComponentsConfiguration", 37121, 7);
    arrayOfExifTag4[19] = new ExifTag("CompressedBitsPerPixel", 37122, 5);
    arrayOfExifTag4[20] = new ExifTag("ShutterSpeedValue", 37377, 10);
    arrayOfExifTag4[21] = new ExifTag("ApertureValue", 37378, 5);
    arrayOfExifTag4[22] = new ExifTag("BrightnessValue", 37379, 10);
    arrayOfExifTag4[23] = new ExifTag("ExposureBiasValue", 37380, 10);
    arrayOfExifTag4[24] = new ExifTag("MaxApertureValue", 37381, 5);
    arrayOfExifTag4[25] = new ExifTag("SubjectDistance", 37382, 5);
    arrayOfExifTag4[26] = new ExifTag("MeteringMode", 37383, 3);
    arrayOfExifTag4[27] = new ExifTag("LightSource", 37384, 3);
    arrayOfExifTag4[28] = new ExifTag("Flash", 37385, 3);
    arrayOfExifTag4[29] = new ExifTag("FocalLength", 37386, 5);
    arrayOfExifTag4[30] = new ExifTag("SubjectArea", 37396, 3);
    arrayOfExifTag4[31] = new ExifTag("MakerNote", 37500, 7);
    arrayOfExifTag4[32] = new ExifTag("UserComment", 37510, 7);
    arrayOfExifTag4[33] = new ExifTag("SubSecTime", 37520, 2);
    arrayOfExifTag4[34] = new ExifTag("SubSecTimeOriginal", 37521, 2);
    arrayOfExifTag4[35] = new ExifTag("SubSecTimeDigitized", 37522, 2);
    arrayOfExifTag4[36] = new ExifTag("FlashpixVersion", 40960, 7);
    arrayOfExifTag4[37] = new ExifTag("ColorSpace", 40961, 3);
    arrayOfExifTag4[38] = new ExifTag("PixelXDimension", 40962, 3, 4);
    arrayOfExifTag4[39] = new ExifTag("PixelYDimension", 40963, 3, 4);
    arrayOfExifTag4[40] = new ExifTag("RelatedSoundFile", 40964, 2);
    arrayOfExifTag4[41] = new ExifTag("InteroperabilityIFDPointer", 40965, 4);
    arrayOfExifTag4[42] = new ExifTag("FlashEnergy", 41483, 5);
    arrayOfExifTag4[43] = new ExifTag("SpatialFrequencyResponse", 41484, 7);
    arrayOfExifTag4[44] = new ExifTag("FocalPlaneXResolution", 41486, 5);
    arrayOfExifTag4[45] = new ExifTag("FocalPlaneYResolution", 41487, 5);
    arrayOfExifTag4[46] = new ExifTag("FocalPlaneResolutionUnit", 41488, 3);
    arrayOfExifTag4[47] = new ExifTag("SubjectLocation", 41492, 3);
    arrayOfExifTag4[48] = new ExifTag("ExposureIndex", 41493, 5);
    arrayOfExifTag4[49] = new ExifTag("SensingMethod", 41495, 3);
    arrayOfExifTag4[50] = new ExifTag("FileSource", 41728, 7);
    arrayOfExifTag4[51] = new ExifTag("SceneType", 41729, 7);
    arrayOfExifTag4[52] = new ExifTag("CFAPattern", 41730, 7);
    arrayOfExifTag4[53] = new ExifTag("CustomRendered", 41985, 3);
    arrayOfExifTag4[54] = new ExifTag("ExposureMode", 41986, 3);
    arrayOfExifTag4[55] = new ExifTag("WhiteBalance", 41987, 3);
    arrayOfExifTag4[56] = new ExifTag("DigitalZoomRatio", 41988, 5);
    arrayOfExifTag4[57] = new ExifTag("FocalLengthIn35mmFilm", 41989, 3);
    arrayOfExifTag4[58] = new ExifTag("SceneCaptureType", 41990, 3);
    arrayOfExifTag4[59] = new ExifTag("GainControl", 41991, 3);
    arrayOfExifTag4[60] = new ExifTag("Contrast", 41992, 3);
    arrayOfExifTag4[61] = new ExifTag("Saturation", 41993, 3);
    arrayOfExifTag4[62] = new ExifTag("Sharpness", 41994, 3);
    arrayOfExifTag4[63] = new ExifTag("DeviceSettingDescription", 41995, 7);
    arrayOfExifTag4[64] = new ExifTag("SubjectDistanceRange", 41996, 3);
    arrayOfExifTag4[65] = new ExifTag("ImageUniqueID", 42016, 2);
    arrayOfExifTag4[66] = new ExifTag("CameraOwnerName", 42032, 2);
    arrayOfExifTag4[67] = new ExifTag("BodySerialNumber", 42033, 2);
    arrayOfExifTag4[68] = new ExifTag("LensSpecification", 42034, 5);
    arrayOfExifTag4[69] = new ExifTag("LensMake", 42035, 2);
    arrayOfExifTag4[70] = new ExifTag("LensModel", 42036, 2);
    arrayOfExifTag4[71] = new ExifTag("Gamma", 42240, 5);
    arrayOfExifTag4[72] = new ExifTag("DNGVersion", 50706, 1);
    arrayOfExifTag4[73] = new ExifTag("DefaultCropSize", 50720, 3, 4);
    IFD_EXIF_TAGS = arrayOfExifTag4;
    ExifTag[] arrayOfExifTag8 = new ExifTag[32];
    arrayOfExifTag8[0] = new ExifTag("GPSVersionID", 0, 1);
    arrayOfExifTag8[1] = new ExifTag("GPSLatitudeRef", 1, 2);
    arrayOfExifTag8[2] = new ExifTag("GPSLatitude", 2, 5);
    arrayOfExifTag8[3] = new ExifTag("GPSLongitudeRef", 3, 2);
    arrayOfExifTag8[4] = new ExifTag("GPSLongitude", 4, 5);
    arrayOfExifTag8[5] = new ExifTag("GPSAltitudeRef", 5, 1);
    arrayOfExifTag8[6] = new ExifTag("GPSAltitude", 6, 5);
    arrayOfExifTag8[7] = new ExifTag("GPSTimeStamp", 7, 5);
    arrayOfExifTag8[8] = new ExifTag("GPSSatellites", 8, 2);
    arrayOfExifTag8[9] = new ExifTag("GPSStatus", 9, 2);
    arrayOfExifTag8[10] = new ExifTag("GPSMeasureMode", 10, 2);
    arrayOfExifTag8[11] = new ExifTag("GPSDOP", 11, 5);
    arrayOfExifTag8[12] = new ExifTag("GPSSpeedRef", 12, 2);
    arrayOfExifTag8[13] = new ExifTag("GPSSpeed", 13, 5);
    arrayOfExifTag8[14] = new ExifTag("GPSTrackRef", 14, 2);
    arrayOfExifTag8[15] = new ExifTag("GPSTrack", 15, 5);
    arrayOfExifTag8[16] = new ExifTag("GPSImgDirectionRef", 16, 2);
    arrayOfExifTag8[17] = new ExifTag("GPSImgDirection", 17, 5);
    arrayOfExifTag8[18] = new ExifTag("GPSMapDatum", 18, 2);
    arrayOfExifTag8[19] = new ExifTag("GPSDestLatitudeRef", 19, 2);
    arrayOfExifTag8[20] = new ExifTag("GPSDestLatitude", 20, 5);
    arrayOfExifTag8[21] = new ExifTag("GPSDestLongitudeRef", 21, 2);
    arrayOfExifTag8[22] = new ExifTag("GPSDestLongitude", 22, 5);
    arrayOfExifTag8[23] = new ExifTag("GPSDestBearingRef", 23, 2);
    arrayOfExifTag8[24] = new ExifTag("GPSDestBearing", 24, 5);
    arrayOfExifTag8[25] = new ExifTag("GPSDestDistanceRef", 25, 2);
    arrayOfExifTag8[26] = new ExifTag("GPSDestDistance", 26, 5);
    arrayOfExifTag8[27] = new ExifTag("GPSProcessingMethod", 27, 7);
    arrayOfExifTag8[28] = new ExifTag("GPSAreaInformation", 28, 7);
    arrayOfExifTag8[29] = new ExifTag("GPSDateStamp", 29, 2);
    arrayOfExifTag8[30] = new ExifTag("GPSDifferential", 30, 3);
    arrayOfExifTag8[31] = new ExifTag("GPSHPositioningError", 31, 5);
    IFD_GPS_TAGS = arrayOfExifTag8;
    ExifTag[] arrayOfExifTag9 = new ExifTag[1];
    arrayOfExifTag9[0] = new ExifTag("InteroperabilityIndex", 1, 2);
    IFD_INTEROPERABILITY_TAGS = arrayOfExifTag9;
    ExifTag[] arrayOfExifTag1 = new ExifTag[37];
    arrayOfExifTag1[0] = new ExifTag("NewSubfileType", 254, 4);
    arrayOfExifTag1[1] = new ExifTag("SubfileType", 255, 4);
    arrayOfExifTag1[2] = new ExifTag("ThumbnailImageWidth", 256, 3, 4);
    arrayOfExifTag1[3] = new ExifTag("ThumbnailImageLength", 257, 3, 4);
    arrayOfExifTag1[4] = new ExifTag("BitsPerSample", 258, 3);
    arrayOfExifTag1[5] = new ExifTag("Compression", 259, 3);
    arrayOfExifTag1[6] = new ExifTag("PhotometricInterpretation", 262, 3);
    arrayOfExifTag1[7] = new ExifTag("ImageDescription", 270, 2);
    arrayOfExifTag1[8] = new ExifTag("Make", 271, 2);
    arrayOfExifTag1[9] = new ExifTag("Model", 272, 2);
    arrayOfExifTag1[10] = new ExifTag("StripOffsets", 273, 3, 4);
    arrayOfExifTag1[11] = new ExifTag("ThumbnailOrientation", 274, 3);
    arrayOfExifTag1[12] = new ExifTag("SamplesPerPixel", 277, 3);
    arrayOfExifTag1[13] = new ExifTag("RowsPerStrip", 278, 3, 4);
    arrayOfExifTag1[14] = new ExifTag("StripByteCounts", 279, 3, 4);
    arrayOfExifTag1[15] = new ExifTag("XResolution", 282, 5);
    arrayOfExifTag1[16] = new ExifTag("YResolution", 283, 5);
    arrayOfExifTag1[17] = new ExifTag("PlanarConfiguration", 284, 3);
    arrayOfExifTag1[18] = new ExifTag("ResolutionUnit", 296, 3);
    arrayOfExifTag1[19] = new ExifTag("TransferFunction", 301, 3);
    arrayOfExifTag1[20] = new ExifTag("Software", 305, 2);
    arrayOfExifTag1[21] = new ExifTag("DateTime", 306, 2);
    arrayOfExifTag1[22] = new ExifTag("Artist", 315, 2);
    arrayOfExifTag1[23] = new ExifTag("WhitePoint", 318, 5);
    arrayOfExifTag1[24] = new ExifTag("PrimaryChromaticities", 319, 5);
    arrayOfExifTag1[25] = new ExifTag("SubIFDPointer", 330, 4);
    arrayOfExifTag1[26] = new ExifTag("JPEGInterchangeFormat", 513, 4);
    arrayOfExifTag1[27] = new ExifTag("JPEGInterchangeFormatLength", 514, 4);
    arrayOfExifTag1[28] = new ExifTag("YCbCrCoefficients", 529, 5);
    arrayOfExifTag1[29] = new ExifTag("YCbCrSubSampling", 530, 3);
    arrayOfExifTag1[30] = new ExifTag("YCbCrPositioning", 531, 3);
    arrayOfExifTag1[31] = new ExifTag("ReferenceBlackWhite", 532, 5);
    arrayOfExifTag1[32] = new ExifTag("Copyright", 33432, 2);
    arrayOfExifTag1[33] = new ExifTag("ExifIFDPointer", 34665, 4);
    arrayOfExifTag1[34] = new ExifTag("GPSInfoIFDPointer", 34853, 4);
    arrayOfExifTag1[35] = new ExifTag("DNGVersion", 50706, 1);
    arrayOfExifTag1[36] = new ExifTag("DefaultCropSize", 50720, 3, 4);
    IFD_THUMBNAIL_TAGS = arrayOfExifTag1;
    TAG_RAF_IMAGE_SIZE = new ExifTag("StripOffsets", 273, 3);
    ExifTag[] arrayOfExifTag5 = new ExifTag[3];
    arrayOfExifTag5[0] = new ExifTag("ThumbnailImage", 256, 7);
    arrayOfExifTag5[1] = new ExifTag("CameraSettingsIFDPointer", 8224, 4);
    arrayOfExifTag5[2] = new ExifTag("ImageProcessingIFDPointer", 8256, 4);
    ORF_MAKER_NOTE_TAGS = arrayOfExifTag5;
    ExifTag[] arrayOfExifTag6 = new ExifTag[2];
    arrayOfExifTag6[0] = new ExifTag("PreviewImageStart", 257, 4);
    arrayOfExifTag6[1] = new ExifTag("PreviewImageLength", 258, 4);
    ORF_CAMERA_SETTINGS_TAGS = arrayOfExifTag6;
    ExifTag[] arrayOfExifTag7 = new ExifTag[1];
    arrayOfExifTag7[0] = new ExifTag("AspectFrame", 4371, 3);
    ORF_IMAGE_PROCESSING_TAGS = arrayOfExifTag7;
    ExifTag[] arrayOfExifTag2 = new ExifTag[1];
    arrayOfExifTag2[0] = new ExifTag("ColorSpace", 55, 3);
    PEF_TAGS = arrayOfExifTag2;
    ExifTag[][] arrayOfExifTag = new ExifTag[10][];
    arrayOfExifTag[0] = arrayOfExifTag3;
    arrayOfExifTag[1] = arrayOfExifTag4;
    arrayOfExifTag[2] = arrayOfExifTag8;
    arrayOfExifTag[3] = arrayOfExifTag9;
    arrayOfExifTag[4] = arrayOfExifTag1;
    arrayOfExifTag[5] = arrayOfExifTag3;
    arrayOfExifTag[6] = arrayOfExifTag5;
    arrayOfExifTag[7] = arrayOfExifTag6;
    arrayOfExifTag[8] = arrayOfExifTag7;
    arrayOfExifTag[9] = arrayOfExifTag2;
    EXIF_TAGS = arrayOfExifTag;
    EXIF_POINTER_TAGS = new ExifTag[] { new ExifTag("SubIFDPointer", 330, 4), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("CameraSettingsIFDPointer", 8224, 1), new ExifTag("ImageProcessingIFDPointer", 8256, 1) };
    JPEG_INTERCHANGE_FORMAT_TAG = new ExifTag("JPEGInterchangeFormat", 513, 4);
    JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifTag("JPEGInterchangeFormatLength", 514, 4);
    sExifTagMapsForReading = (HashMap<Integer, ExifTag>[])new HashMap[arrayOfExifTag.length];
    sExifTagMapsForWriting = (HashMap<String, ExifTag>[])new HashMap[arrayOfExifTag.length];
    sTagSetForCompatibility = new HashSet<>(Arrays.asList(new String[] { "FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp" }));
    sExifPointerTagMap = new HashMap<>();
    Charset charset = Charset.forName("US-ASCII");
    ASCII = charset;
    IDENTIFIER_EXIF_APP1 = "Exif\000\000".getBytes(charset);
    IDENTIFIER_XMP_APP1 = "http://ns.adobe.com/xap/1.0/\000".getBytes(charset);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    sFormatter = simpleDateFormat;
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    byte b = 0;
    while (true) {
      ExifTag[][] arrayOfExifTag10 = EXIF_TAGS;
      if (b < arrayOfExifTag10.length) {
        sExifTagMapsForReading[b] = new HashMap<>();
        sExifTagMapsForWriting[b] = new HashMap<>();
        for (ExifTag exifTag : arrayOfExifTag10[b]) {
          sExifTagMapsForReading[b].put(Integer.valueOf(exifTag.number), exifTag);
          sExifTagMapsForWriting[b].put(exifTag.name, exifTag);
        } 
        b++;
        continue;
      } 
      HashMap<Integer, Integer> hashMap = sExifPointerTagMap;
      arrayOfExifTag2 = EXIF_POINTER_TAGS;
      hashMap.put(Integer.valueOf((arrayOfExifTag2[0]).number), integer3);
      hashMap.put(Integer.valueOf((arrayOfExifTag2[1]).number), integer6);
      hashMap.put(Integer.valueOf((arrayOfExifTag2[2]).number), integer2);
      hashMap.put(Integer.valueOf((arrayOfExifTag2[3]).number), integer4);
      hashMap.put(Integer.valueOf((arrayOfExifTag2[4]).number), integer5);
      hashMap.put(Integer.valueOf((arrayOfExifTag2[5]).number), integer1);
      sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
      sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
      return;
    } 
  }
  
  public ExifInterface(File paramFile) throws IOException {
    ExifTag[][] arrayOfExifTag = EXIF_TAGS;
    this.mAttributes = (HashMap<String, ExifAttribute>[])new HashMap[arrayOfExifTag.length];
    this.mAttributesOffsets = new HashSet<>(arrayOfExifTag.length);
    this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
    if (paramFile != null) {
      initForFilename(paramFile.getAbsolutePath());
      return;
    } 
    throw new NullPointerException("file cannot be null");
  }
  
  public ExifInterface(FileDescriptor paramFileDescriptor) throws IOException {
    ExifTag[][] arrayOfExifTag = EXIF_TAGS;
    this.mAttributes = (HashMap<String, ExifAttribute>[])new HashMap[arrayOfExifTag.length];
    this.mAttributesOffsets = new HashSet<>(arrayOfExifTag.length);
    this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
    if (paramFileDescriptor != null) {
      FileInputStream fileInputStream;
      this.mAssetInputStream = null;
      this.mFilename = null;
      boolean bool = false;
      if (Build.VERSION.SDK_INT >= 21 && isSeekableFD(paramFileDescriptor)) {
        this.mSeekableFileDescriptor = paramFileDescriptor;
        try {
          paramFileDescriptor = Os.dup(paramFileDescriptor);
          bool = true;
        } catch (Exception exception) {
          throw new IOException("Failed to duplicate file descriptor", exception);
        } 
      } else {
        this.mSeekableFileDescriptor = null;
      } 
      ExifTag[][] arrayOfExifTag1 = null;
      arrayOfExifTag = arrayOfExifTag1;
      try {
        FileInputStream fileInputStream1 = new FileInputStream();
        arrayOfExifTag = arrayOfExifTag1;
        this((FileDescriptor)exception);
        fileInputStream = fileInputStream1;
        loadAttributes(fileInputStream1);
        return;
      } finally {
        closeQuietly(fileInputStream);
        if (bool)
          closeFileDescriptor((FileDescriptor)exception); 
      } 
    } 
    throw new NullPointerException("fileDescriptor cannot be null");
  }
  
  public ExifInterface(InputStream paramInputStream) throws IOException {
    this(paramInputStream, false);
  }
  
  public ExifInterface(InputStream paramInputStream, int paramInt) throws IOException {
    this(paramInputStream, bool);
  }
  
  private ExifInterface(InputStream paramInputStream, boolean paramBoolean) throws IOException {
    ExifTag[][] arrayOfExifTag = EXIF_TAGS;
    this.mAttributes = (HashMap<String, ExifAttribute>[])new HashMap[arrayOfExifTag.length];
    this.mAttributesOffsets = new HashSet<>(arrayOfExifTag.length);
    this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
    if (paramInputStream != null) {
      this.mFilename = null;
      if (paramBoolean) {
        paramInputStream = new BufferedInputStream(paramInputStream, 5000);
        if (!isExifDataOnly((BufferedInputStream)paramInputStream)) {
          Log.w("ExifInterface", "Given data does not follow the structure of an Exif-only data.");
          return;
        } 
        this.mIsExifDataOnly = true;
        this.mAssetInputStream = null;
        this.mSeekableFileDescriptor = null;
      } else if (paramInputStream instanceof AssetManager.AssetInputStream) {
        this.mAssetInputStream = (AssetManager.AssetInputStream)paramInputStream;
        this.mSeekableFileDescriptor = null;
      } else if (paramInputStream instanceof FileInputStream && isSeekableFD(((FileInputStream)paramInputStream).getFD())) {
        this.mAssetInputStream = null;
        this.mSeekableFileDescriptor = ((FileInputStream)paramInputStream).getFD();
      } else {
        this.mAssetInputStream = null;
        this.mSeekableFileDescriptor = null;
      } 
      loadAttributes(paramInputStream);
      return;
    } 
    throw new NullPointerException("inputStream cannot be null");
  }
  
  public ExifInterface(String paramString) throws IOException {
    ExifTag[][] arrayOfExifTag = EXIF_TAGS;
    this.mAttributes = (HashMap<String, ExifAttribute>[])new HashMap[arrayOfExifTag.length];
    this.mAttributesOffsets = new HashSet<>(arrayOfExifTag.length);
    this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
    if (paramString != null) {
      initForFilename(paramString);
      return;
    } 
    throw new NullPointerException("filename cannot be null");
  }
  
  private void addDefaultValuesForCompatibility() {
    String str = getAttribute("DateTimeOriginal");
    if (str != null && getAttribute("DateTime") == null)
      this.mAttributes[0].put("DateTime", ExifAttribute.createString(str)); 
    if (getAttribute("ImageWidth") == null)
      this.mAttributes[0].put("ImageWidth", ExifAttribute.createULong(0L, this.mExifByteOrder)); 
    if (getAttribute("ImageLength") == null)
      this.mAttributes[0].put("ImageLength", ExifAttribute.createULong(0L, this.mExifByteOrder)); 
    if (getAttribute("Orientation") == null)
      this.mAttributes[0].put("Orientation", ExifAttribute.createULong(0L, this.mExifByteOrder)); 
    if (getAttribute("LightSource") == null)
      this.mAttributes[1].put("LightSource", ExifAttribute.createULong(0L, this.mExifByteOrder)); 
  }
  
  private static String byteArrayToHexString(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder(paramArrayOfbyte.length * 2);
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      stringBuilder.append(String.format("%02x", new Object[] { Byte.valueOf(paramArrayOfbyte[b]) }));
    } 
    return stringBuilder.toString();
  }
  
  private static void closeFileDescriptor(FileDescriptor paramFileDescriptor) {
    if (Build.VERSION.SDK_INT >= 21) {
      try {
        Os.close(paramFileDescriptor);
      } catch (Exception exception) {
        Log.e("ExifInterface", "Error closing fd.");
      } 
    } else {
      Log.e("ExifInterface", "closeFileDescriptor is called in API < 21, which must be wrong.");
    } 
  }
  
  private static void closeQuietly(Closeable paramCloseable) {
    if (paramCloseable != null)
      try {
        paramCloseable.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  private String convertDecimalDegree(double paramDouble) {
    long l1 = (long)paramDouble;
    long l2 = (long)((paramDouble - l1) * 60.0D);
    long l3 = Math.round((paramDouble - l1 - l2 / 60.0D) * 3600.0D * 1.0E7D);
    return l1 + "/1," + l2 + "/1," + l3 + "/10000000";
  }
  
  private static double convertRationalLatLonToDouble(String paramString1, String paramString2) {
    try {
      String[] arrayOfString1 = paramString1.split(",", -1);
      String[] arrayOfString2 = arrayOfString1[0].split("/", -1);
      double d1 = Double.parseDouble(arrayOfString2[0].trim()) / Double.parseDouble(arrayOfString2[1].trim());
      arrayOfString2 = arrayOfString1[1].split("/", -1);
      double d3 = Double.parseDouble(arrayOfString2[0].trim()) / Double.parseDouble(arrayOfString2[1].trim());
      arrayOfString1 = arrayOfString1[2].split("/", -1);
      double d2 = Double.parseDouble(arrayOfString1[0].trim()) / Double.parseDouble(arrayOfString1[1].trim());
      d1 = d3 / 60.0D + d1 + d2 / 3600.0D;
      if (paramString2.equals("S") || paramString2.equals("W"))
        return -d1; 
      if (paramString2.equals("N") || paramString2.equals("E"))
        return d1; 
      IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
      this();
      throw illegalArgumentException;
    } catch (NumberFormatException|ArrayIndexOutOfBoundsException numberFormatException) {
      throw new IllegalArgumentException();
    } 
  }
  
  private static long[] convertToLongArray(Object paramObject) {
    if (paramObject instanceof int[]) {
      paramObject = paramObject;
      long[] arrayOfLong = new long[paramObject.length];
      for (byte b = 0; b < paramObject.length; b++)
        arrayOfLong[b] = paramObject[b]; 
      return arrayOfLong;
    } 
    return (paramObject instanceof long[]) ? (long[])paramObject : null;
  }
  
  private static int copy(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException {
    int i = 0;
    byte[] arrayOfByte = new byte[8192];
    while (true) {
      int j = paramInputStream.read(arrayOfByte);
      if (j != -1) {
        i += j;
        paramOutputStream.write(arrayOfByte, 0, j);
        continue;
      } 
      return i;
    } 
  }
  
  private static void copy(InputStream paramInputStream, OutputStream paramOutputStream, int paramInt) throws IOException {
    byte[] arrayOfByte = new byte[8192];
    while (paramInt > 0) {
      int j = Math.min(paramInt, 8192);
      int i = paramInputStream.read(arrayOfByte, 0, j);
      if (i == j) {
        paramInt -= i;
        paramOutputStream.write(arrayOfByte, 0, i);
        continue;
      } 
      throw new IOException("Failed to copy the given amount of bytes from the inputstream to the output stream.");
    } 
  }
  
  private void copyChunksUpToGivenChunkType(ByteOrderedDataInputStream paramByteOrderedDataInputStream, ByteOrderedDataOutputStream paramByteOrderedDataOutputStream, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws IOException {
    byte[] arrayOfByte;
    do {
      String str;
      StringBuilder stringBuilder;
      arrayOfByte = new byte[4];
      if (paramByteOrderedDataInputStream.read(arrayOfByte) != arrayOfByte.length) {
        stringBuilder = (new StringBuilder()).append("Encountered invalid length while copying WebP chunks up tochunk type ");
        Charset charset = ASCII;
        stringBuilder = stringBuilder.append(new String(paramArrayOfbyte1, charset));
        if (paramArrayOfbyte2 == null) {
          str = "";
        } else {
          str = " or " + new String(paramArrayOfbyte2, (Charset)str);
        } 
        throw new IOException(stringBuilder.append(str).toString());
      } 
      copyWebPChunk((ByteOrderedDataInputStream)str, (ByteOrderedDataOutputStream)stringBuilder, arrayOfByte);
    } while (!Arrays.equals(arrayOfByte, paramArrayOfbyte1) && (paramArrayOfbyte2 == null || !Arrays.equals(arrayOfByte, paramArrayOfbyte2)));
  }
  
  private void copyWebPChunk(ByteOrderedDataInputStream paramByteOrderedDataInputStream, ByteOrderedDataOutputStream paramByteOrderedDataOutputStream, byte[] paramArrayOfbyte) throws IOException {
    int i = paramByteOrderedDataInputStream.readInt();
    paramByteOrderedDataOutputStream.write(paramArrayOfbyte);
    paramByteOrderedDataOutputStream.writeInt(i);
    if (i % 2 == 1)
      i++; 
    copy(paramByteOrderedDataInputStream, paramByteOrderedDataOutputStream, i);
  }
  
  private ExifAttribute getExifAttribute(String paramString) {
    if (paramString != null) {
      String str = paramString;
      if ("ISOSpeedRatings".equals(paramString)) {
        if (DEBUG)
          Log.d("ExifInterface", "getExifAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY."); 
        str = "PhotographicSensitivity";
      } 
      for (byte b = 0; b < EXIF_TAGS.length; b++) {
        ExifAttribute exifAttribute = this.mAttributes[b].get(str);
        if (exifAttribute != null)
          return exifAttribute; 
      } 
      return null;
    } 
    throw new NullPointerException("tag shouldn't be null");
  }
  
  private void getHeifAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream) throws IOException {
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    try {
      if (Build.VERSION.SDK_INT >= 23) {
        MediaDataSource mediaDataSource = new MediaDataSource() {
            long mPosition;
            
            final ExifInterface this$0;
            
            final ExifInterface.ByteOrderedDataInputStream val$in;
            
            public void close() throws IOException {}
            
            public long getSize() throws IOException {
              return -1L;
            }
            
            public int readAt(long param1Long, byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
              if (param1Int2 == 0)
                return 0; 
              if (param1Long < 0L)
                return -1; 
              try {
                long l = this.mPosition;
                if (l != param1Long) {
                  if (l >= 0L && param1Long >= l + in.available())
                    return -1; 
                  in.seek(param1Long);
                  this.mPosition = param1Long;
                } 
                int i = param1Int2;
                if (param1Int2 > in.available())
                  i = in.available(); 
                param1Int1 = in.read(param1ArrayOfbyte, param1Int1, i);
                if (param1Int1 >= 0) {
                  this.mPosition += param1Int1;
                  return param1Int1;
                } 
              } catch (IOException iOException) {}
              this.mPosition = -1L;
              return -1;
            }
          };
        super(this, paramByteOrderedDataInputStream);
        mediaMetadataRetriever.setDataSource(mediaDataSource);
      } else {
        FileDescriptor fileDescriptor = this.mSeekableFileDescriptor;
        if (fileDescriptor != null) {
          mediaMetadataRetriever.setDataSource(fileDescriptor);
        } else {
          String str = this.mFilename;
          if (str != null) {
            mediaMetadataRetriever.setDataSource(str);
          } else {
            mediaMetadataRetriever.release();
            return;
          } 
        } 
      } 
      String str4 = mediaMetadataRetriever.extractMetadata(33);
      String str5 = mediaMetadataRetriever.extractMetadata(34);
      String str7 = mediaMetadataRetriever.extractMetadata(26);
      String str6 = mediaMetadataRetriever.extractMetadata(17);
      String str1 = null;
      String str2 = null;
      String str3 = null;
      if ("yes".equals(str7)) {
        str1 = mediaMetadataRetriever.extractMetadata(29);
        str2 = mediaMetadataRetriever.extractMetadata(30);
        str3 = mediaMetadataRetriever.extractMetadata(31);
      } else if ("yes".equals(str6)) {
        str1 = mediaMetadataRetriever.extractMetadata(18);
        str2 = mediaMetadataRetriever.extractMetadata(19);
        str3 = mediaMetadataRetriever.extractMetadata(24);
      } 
      if (str1 != null)
        this.mAttributes[0].put("ImageWidth", ExifAttribute.createUShort(Integer.parseInt(str1), this.mExifByteOrder)); 
      if (str2 != null)
        this.mAttributes[0].put("ImageLength", ExifAttribute.createUShort(Integer.parseInt(str2), this.mExifByteOrder)); 
      if (str3 != null) {
        byte b = 1;
        switch (Integer.parseInt(str3)) {
          case 270:
            b = 8;
            break;
          case 180:
            b = 3;
            break;
          case 90:
            b = 6;
            break;
        } 
        this.mAttributes[0].put("Orientation", ExifAttribute.createUShort(b, this.mExifByteOrder));
      } 
      if (str4 != null && str5 != null) {
        IOException iOException;
        int i = Integer.parseInt(str4);
        int j = Integer.parseInt(str5);
        if (j > 6) {
          long l = i;
          try {
            paramByteOrderedDataInputStream.seek(l);
            byte[] arrayOfByte = new byte[6];
            if (paramByteOrderedDataInputStream.read(arrayOfByte) == 6) {
              int k = j - 6;
              if (Arrays.equals(arrayOfByte, IDENTIFIER_EXIF_APP1)) {
                arrayOfByte = new byte[k];
                j = paramByteOrderedDataInputStream.read(arrayOfByte);
                if (j == k) {
                  try {
                    this.mExifOffset = i + 6;
                    readExifSegment(arrayOfByte, 0);
                    if (DEBUG) {
                      StringBuilder stringBuilder = new StringBuilder();
                      this();
                      Log.d("ExifInterface", stringBuilder.append("Heif meta: ").append(str1).append("x").append(str2).append(", rotation ").append(str3).toString());
                    } 
                    mediaMetadataRetriever.release();
                    return;
                  } finally {}
                } else {
                  iOException = new IOException();
                  this("Can't read exif");
                  throw iOException;
                } 
              } else {
                iOException = new IOException();
                this("Invalid identifier");
                throw iOException;
              } 
            } else {
              iOException = new IOException();
              this("Can't read identifier");
              throw iOException;
            } 
          } finally {}
        } else {
          iOException = new IOException();
          this("Invalid exif length");
          throw iOException;
        } 
        mediaMetadataRetriever.release();
        throw iOException;
      } 
      if (DEBUG) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Log.d("ExifInterface", stringBuilder.append("Heif meta: ").append(str1).append("x").append(str2).append(", rotation ").append(str3).toString());
      } 
      mediaMetadataRetriever.release();
      return;
    } finally {}
    mediaMetadataRetriever.release();
    throw paramByteOrderedDataInputStream;
  }
  
  private void getJpegAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream, int paramInt1, int paramInt2) throws IOException {
    boolean bool = DEBUG;
    String str = "ExifInterface";
    if (bool)
      Log.d("ExifInterface", "getJpegAttributes starting with: " + paramByteOrderedDataInputStream); 
    paramByteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
    paramByteOrderedDataInputStream.seek(paramInt1);
    int i = paramByteOrderedDataInputStream.readByte();
    if (i == -1) {
      int j = 1;
      if (paramByteOrderedDataInputStream.readByte() == -40) {
        i = paramInt1 + 1 + 1;
        paramInt1 = j;
        j = i;
        while (true) {
          i = paramByteOrderedDataInputStream.readByte();
          if (i == -1) {
            byte b = paramByteOrderedDataInputStream.readByte();
            bool = DEBUG;
            if (bool)
              Log.d(str, "Found JPEG segment indicator: " + Integer.toHexString(b & 0xFF)); 
            if (b != -39) {
              if (b == -38)
                continue; 
              int k = paramByteOrderedDataInputStream.readUnsignedShort() - 2;
              i = j + 1 + paramInt1 + 2;
              if (bool)
                Log.d(str, "JPEG segment: " + Integer.toHexString(b & 0xFF) + " (length: " + (k + 2) + ")"); 
              if (k >= 0) {
                byte[] arrayOfByte1;
                byte[] arrayOfByte2;
                switch (b) {
                  default:
                    j = k;
                    break;
                  case -2:
                    arrayOfByte1 = new byte[k];
                    if (paramByteOrderedDataInputStream.read(arrayOfByte1) == k) {
                      j = 0;
                      if (getAttribute("UserComment") == null)
                        this.mAttributes[paramInt1].put("UserComment", ExifAttribute.createString(new String(arrayOfByte1, ASCII))); 
                      break;
                    } 
                    throw new IOException("Invalid exif");
                  case -31:
                    arrayOfByte1 = new byte[k];
                    paramByteOrderedDataInputStream.readFully(arrayOfByte1);
                    k = i + k;
                    j = 0;
                    arrayOfByte2 = IDENTIFIER_EXIF_APP1;
                    if (startsWith(arrayOfByte1, arrayOfByte2)) {
                      int m = arrayOfByte2.length;
                      arrayOfByte1 = Arrays.copyOfRange(arrayOfByte1, arrayOfByte2.length, arrayOfByte1.length);
                      this.mExifOffset = m + i;
                      readExifSegment(arrayOfByte1, paramInt2);
                    } else {
                      arrayOfByte2 = IDENTIFIER_XMP_APP1;
                      if (startsWith(arrayOfByte1, arrayOfByte2)) {
                        int m = arrayOfByte2.length;
                        arrayOfByte1 = Arrays.copyOfRange(arrayOfByte1, arrayOfByte2.length, arrayOfByte1.length);
                        if (getAttribute("Xmp") == null) {
                          this.mAttributes[0].put("Xmp", new ExifAttribute(1, arrayOfByte1.length, (m + i), arrayOfByte1));
                          paramInt1 = 1;
                          this.mXmpIsFromSeparateMarker = true;
                        } 
                      } 
                    } 
                    i = k;
                    break;
                  case -64:
                  case -63:
                  case -62:
                  case -61:
                  case -59:
                  case -58:
                  case -57:
                  case -55:
                  case -54:
                  case -53:
                  case -51:
                  case -50:
                  case -49:
                    if (paramByteOrderedDataInputStream.skipBytes(paramInt1) == paramInt1) {
                      this.mAttributes[paramInt2].put("ImageLength", ExifAttribute.createULong(paramByteOrderedDataInputStream.readUnsignedShort(), this.mExifByteOrder));
                      this.mAttributes[paramInt2].put("ImageWidth", ExifAttribute.createULong(paramByteOrderedDataInputStream.readUnsignedShort(), this.mExifByteOrder));
                      j = k - 5;
                      break;
                    } 
                    throw new IOException("Invalid SOFx");
                } 
                if (j >= 0) {
                  if (paramByteOrderedDataInputStream.skipBytes(j) == j) {
                    j = i + j;
                    continue;
                  } 
                  throw new IOException("Invalid JPEG segment");
                } 
                throw new IOException("Invalid length");
              } 
              throw new IOException("Invalid length");
            } 
            paramByteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
            return;
          } 
          throw new IOException("Invalid marker:" + Integer.toHexString(i & 0xFF));
        } 
      } 
      throw new IOException("Invalid marker: " + Integer.toHexString(i & 0xFF));
    } 
    throw new IOException("Invalid marker: " + Integer.toHexString(i & 0xFF));
  }
  
  private int getMimeType(BufferedInputStream paramBufferedInputStream) throws IOException {
    paramBufferedInputStream.mark(5000);
    byte[] arrayOfByte = new byte[5000];
    paramBufferedInputStream.read(arrayOfByte);
    paramBufferedInputStream.reset();
    return isJpegFormat(arrayOfByte) ? 4 : (isRafFormat(arrayOfByte) ? 9 : (isHeifFormat(arrayOfByte) ? 12 : (isOrfFormat(arrayOfByte) ? 7 : (isRw2Format(arrayOfByte) ? 10 : (isPngFormat(arrayOfByte) ? 13 : (isWebpFormat(arrayOfByte) ? 14 : 0))))));
  }
  
  private void getOrfAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream) throws IOException {
    getRawAttributes(paramByteOrderedDataInputStream);
    ExifAttribute exifAttribute = this.mAttributes[1].get("MakerNote");
    if (exifAttribute != null) {
      ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(exifAttribute.bytes);
      byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
      byte[] arrayOfByte3 = ORF_MAKER_NOTE_HEADER_1;
      byte[] arrayOfByte1 = new byte[arrayOfByte3.length];
      byteOrderedDataInputStream.readFully(arrayOfByte1);
      byteOrderedDataInputStream.seek(0L);
      byte[] arrayOfByte4 = ORF_MAKER_NOTE_HEADER_2;
      byte[] arrayOfByte2 = new byte[arrayOfByte4.length];
      byteOrderedDataInputStream.readFully(arrayOfByte2);
      if (Arrays.equals(arrayOfByte1, arrayOfByte3)) {
        byteOrderedDataInputStream.seek(8L);
      } else if (Arrays.equals(arrayOfByte2, arrayOfByte4)) {
        byteOrderedDataInputStream.seek(12L);
      } 
      readImageFileDirectory(byteOrderedDataInputStream, 6);
      ExifAttribute exifAttribute1 = this.mAttributes[7].get("PreviewImageStart");
      ExifAttribute exifAttribute2 = this.mAttributes[7].get("PreviewImageLength");
      if (exifAttribute1 != null && exifAttribute2 != null) {
        this.mAttributes[5].put("JPEGInterchangeFormat", exifAttribute1);
        this.mAttributes[5].put("JPEGInterchangeFormatLength", exifAttribute2);
      } 
      exifAttribute1 = this.mAttributes[8].get("AspectFrame");
      if (exifAttribute1 != null) {
        int[] arrayOfInt = (int[])exifAttribute1.getValue(this.mExifByteOrder);
        if (arrayOfInt == null || arrayOfInt.length != 4) {
          Log.w("ExifInterface", "Invalid aspect frame values. frame=" + Arrays.toString(arrayOfInt));
          return;
        } 
        if (arrayOfInt[2] > arrayOfInt[0] && arrayOfInt[3] > arrayOfInt[1]) {
          int m = arrayOfInt[2] - arrayOfInt[0] + 1;
          int k = arrayOfInt[3] - arrayOfInt[1] + 1;
          int j = m;
          int i = k;
          if (m < k) {
            j = m + k;
            i = j - k;
            j -= i;
          } 
          exifAttribute2 = ExifAttribute.createUShort(j, this.mExifByteOrder);
          ExifAttribute exifAttribute3 = ExifAttribute.createUShort(i, this.mExifByteOrder);
          this.mAttributes[0].put("ImageWidth", exifAttribute2);
          this.mAttributes[0].put("ImageLength", exifAttribute3);
        } 
      } 
    } 
  }
  
  private void getPngAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream) throws IOException {
    if (DEBUG)
      Log.d("ExifInterface", "getPngAttributes starting with: " + paramByteOrderedDataInputStream); 
    paramByteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
    byte[] arrayOfByte = PNG_SIGNATURE;
    paramByteOrderedDataInputStream.skipBytes(arrayOfByte.length);
    int i = 0 + arrayOfByte.length;
    try {
      while (true) {
        int j = paramByteOrderedDataInputStream.readInt();
        arrayOfByte = new byte[4];
        if (paramByteOrderedDataInputStream.read(arrayOfByte) == arrayOfByte.length) {
          i = i + 4 + 4;
          if (i != 16 || Arrays.equals(arrayOfByte, PNG_CHUNK_TYPE_IHDR)) {
            if (!Arrays.equals(arrayOfByte, PNG_CHUNK_TYPE_IEND)) {
              StringBuilder stringBuilder;
              if (Arrays.equals(arrayOfByte, PNG_CHUNK_TYPE_EXIF)) {
                StringBuilder stringBuilder1;
                byte[] arrayOfByte1 = new byte[j];
                if (paramByteOrderedDataInputStream.read(arrayOfByte1) == j) {
                  j = paramByteOrderedDataInputStream.readInt();
                  CRC32 cRC32 = new CRC32();
                  this();
                  cRC32.update(arrayOfByte);
                  cRC32.update(arrayOfByte1);
                  if ((int)cRC32.getValue() == j) {
                    this.mExifOffset = i;
                    readExifSegment(arrayOfByte1, 0);
                    validateImages();
                    return;
                  } 
                  IOException iOException3 = new IOException();
                  stringBuilder1 = new StringBuilder();
                  this();
                  this(stringBuilder1.append("Encountered invalid CRC value for PNG-EXIF chunk.\n recorded CRC value: ").append(j).append(", calculated CRC value: ").append(cRC32.getValue()).toString());
                  throw iOException3;
                } 
                IOException iOException2 = new IOException();
                stringBuilder = new StringBuilder();
                this();
                this(stringBuilder.append("Failed to read given length for given PNG chunk type: ").append(byteArrayToHexString((byte[])stringBuilder1)).toString());
                throw iOException2;
              } 
              stringBuilder.skipBytes(j + 4);
              i += j + 4;
              continue;
            } 
            return;
          } 
          IOException iOException1 = new IOException();
          this("Encountered invalid PNG file--IHDR chunk should appearas the first chunk");
          throw iOException1;
        } 
        IOException iOException = new IOException();
        this("Encountered invalid length while parsing PNG chunktype");
        throw iOException;
      } 
    } catch (EOFException eOFException) {
      throw new IOException("Encountered corrupt PNG file.");
    } 
  }
  
  private void getRafAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream) throws IOException {
    paramByteOrderedDataInputStream.skipBytes(84);
    byte[] arrayOfByte2 = new byte[4];
    byte[] arrayOfByte1 = new byte[4];
    paramByteOrderedDataInputStream.read(arrayOfByte2);
    paramByteOrderedDataInputStream.skipBytes(4);
    paramByteOrderedDataInputStream.read(arrayOfByte1);
    int i = ByteBuffer.wrap(arrayOfByte2).getInt();
    int j = ByteBuffer.wrap(arrayOfByte1).getInt();
    getJpegAttributes(paramByteOrderedDataInputStream, i, 5);
    paramByteOrderedDataInputStream.seek(j);
    paramByteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
    j = paramByteOrderedDataInputStream.readInt();
    if (DEBUG)
      Log.d("ExifInterface", "numberOfDirectoryEntry: " + j); 
    for (i = 0; i < j; i++) {
      ExifAttribute exifAttribute;
      int m = paramByteOrderedDataInputStream.readUnsignedShort();
      int k = paramByteOrderedDataInputStream.readUnsignedShort();
      if (m == TAG_RAF_IMAGE_SIZE.number) {
        j = paramByteOrderedDataInputStream.readShort();
        i = paramByteOrderedDataInputStream.readShort();
        ExifAttribute exifAttribute1 = ExifAttribute.createUShort(j, this.mExifByteOrder);
        exifAttribute = ExifAttribute.createUShort(i, this.mExifByteOrder);
        this.mAttributes[0].put("ImageLength", exifAttribute1);
        this.mAttributes[0].put("ImageWidth", exifAttribute);
        if (DEBUG)
          Log.d("ExifInterface", "Updated to length: " + j + ", width: " + i); 
        return;
      } 
      exifAttribute.skipBytes(k);
    } 
  }
  
  private void getRawAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream) throws IOException {
    parseTiffHeaders(paramByteOrderedDataInputStream, paramByteOrderedDataInputStream.available());
    readImageFileDirectory(paramByteOrderedDataInputStream, 0);
    updateImageSizeValues(paramByteOrderedDataInputStream, 0);
    updateImageSizeValues(paramByteOrderedDataInputStream, 5);
    updateImageSizeValues(paramByteOrderedDataInputStream, 4);
    validateImages();
    if (this.mMimeType == 8) {
      ExifAttribute exifAttribute = this.mAttributes[1].get("MakerNote");
      if (exifAttribute != null) {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(exifAttribute.bytes);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
        byteOrderedDataInputStream.seek(6L);
        readImageFileDirectory(byteOrderedDataInputStream, 9);
        ExifAttribute exifAttribute1 = this.mAttributes[9].get("ColorSpace");
        if (exifAttribute1 != null)
          this.mAttributes[1].put("ColorSpace", exifAttribute1); 
      } 
    } 
  }
  
  private void getRw2Attributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream) throws IOException {
    getRawAttributes(paramByteOrderedDataInputStream);
    if ((ExifAttribute)this.mAttributes[0].get("JpgFromRaw") != null)
      getJpegAttributes(paramByteOrderedDataInputStream, this.mRw2JpgFromRawOffset, 5); 
    ExifAttribute exifAttribute2 = this.mAttributes[0].get("ISO");
    ExifAttribute exifAttribute1 = this.mAttributes[1].get("PhotographicSensitivity");
    if (exifAttribute2 != null && exifAttribute1 == null)
      this.mAttributes[1].put("PhotographicSensitivity", exifAttribute2); 
  }
  
  private void getStandaloneAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream) throws IOException {
    byte[] arrayOfByte1 = IDENTIFIER_EXIF_APP1;
    paramByteOrderedDataInputStream.skipBytes(arrayOfByte1.length);
    byte[] arrayOfByte2 = new byte[paramByteOrderedDataInputStream.available()];
    paramByteOrderedDataInputStream.readFully(arrayOfByte2);
    this.mExifOffset = arrayOfByte1.length;
    readExifSegment(arrayOfByte2, 0);
  }
  
  private void getWebpAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream) throws IOException {
    if (DEBUG)
      Log.d("ExifInterface", "getWebpAttributes starting with: " + paramByteOrderedDataInputStream); 
    paramByteOrderedDataInputStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    paramByteOrderedDataInputStream.skipBytes(WEBP_SIGNATURE_1.length);
    int j = paramByteOrderedDataInputStream.readInt() + 8;
    int i = 8 + paramByteOrderedDataInputStream.skipBytes(WEBP_SIGNATURE_2.length);
    try {
      while (true) {
        byte[] arrayOfByte = new byte[4];
        if (paramByteOrderedDataInputStream.read(arrayOfByte) == arrayOfByte.length) {
          StringBuilder stringBuilder;
          int k = paramByteOrderedDataInputStream.readInt();
          int m = i + 4 + 4;
          if (Arrays.equals(WEBP_CHUNK_TYPE_EXIF, arrayOfByte)) {
            byte[] arrayOfByte1 = new byte[k];
            if (paramByteOrderedDataInputStream.read(arrayOfByte1) == k) {
              this.mExifOffset = m;
              readExifSegment(arrayOfByte1, 0);
              this.mExifOffset = m;
            } else {
              IOException iOException1 = new IOException();
              stringBuilder = new StringBuilder();
              this();
              this(stringBuilder.append("Failed to read given length for given PNG chunk type: ").append(byteArrayToHexString(arrayOfByte)).toString());
              throw iOException1;
            } 
          } else {
            if (k % 2 == 1) {
              i = k + 1;
            } else {
              i = k;
            } 
            if (m + i != j) {
              if (m + i <= j) {
                k = stringBuilder.skipBytes(i);
                if (k == i) {
                  i = m + k;
                  continue;
                } 
                IOException iOException2 = new IOException();
                this("Encountered WebP file with invalid chunk size");
                throw iOException2;
              } 
              IOException iOException1 = new IOException();
              this("Encountered WebP file with invalid chunk size");
              throw iOException1;
            } 
          } 
          return;
        } 
        IOException iOException = new IOException();
        this("Encountered invalid length while parsing WebP chunktype");
        throw iOException;
      } 
    } catch (EOFException eOFException) {
      throw new IOException("Encountered corrupt WebP file.");
    } 
  }
  
  private static Pair<Integer, Integer> guessDataFormat(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: ldc_w ','
    //   4: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   7: istore #9
    //   9: iconst_2
    //   10: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   13: astore #11
    //   15: iconst_m1
    //   16: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   19: astore #10
    //   21: iload #9
    //   23: ifeq -> 268
    //   26: aload_0
    //   27: ldc_w ','
    //   30: iconst_m1
    //   31: invokevirtual split : (Ljava/lang/String;I)[Ljava/lang/String;
    //   34: astore #12
    //   36: aload #12
    //   38: iconst_0
    //   39: aaload
    //   40: invokestatic guessDataFormat : (Ljava/lang/String;)Landroid/util/Pair;
    //   43: astore_0
    //   44: aload_0
    //   45: getfield first : Ljava/lang/Object;
    //   48: checkcast java/lang/Integer
    //   51: invokevirtual intValue : ()I
    //   54: iconst_2
    //   55: if_icmpne -> 60
    //   58: aload_0
    //   59: areturn
    //   60: iconst_1
    //   61: istore_1
    //   62: iload_1
    //   63: aload #12
    //   65: arraylength
    //   66: if_icmpge -> 266
    //   69: aload #12
    //   71: iload_1
    //   72: aaload
    //   73: invokestatic guessDataFormat : (Ljava/lang/String;)Landroid/util/Pair;
    //   76: astore #13
    //   78: iconst_m1
    //   79: istore_2
    //   80: iconst_m1
    //   81: istore #4
    //   83: aload #13
    //   85: getfield first : Ljava/lang/Object;
    //   88: checkcast java/lang/Integer
    //   91: aload_0
    //   92: getfield first : Ljava/lang/Object;
    //   95: invokevirtual equals : (Ljava/lang/Object;)Z
    //   98: ifne -> 119
    //   101: aload #13
    //   103: getfield second : Ljava/lang/Object;
    //   106: checkcast java/lang/Integer
    //   109: aload_0
    //   110: getfield first : Ljava/lang/Object;
    //   113: invokevirtual equals : (Ljava/lang/Object;)Z
    //   116: ifeq -> 130
    //   119: aload_0
    //   120: getfield first : Ljava/lang/Object;
    //   123: checkcast java/lang/Integer
    //   126: invokevirtual intValue : ()I
    //   129: istore_2
    //   130: iload #4
    //   132: istore_3
    //   133: aload_0
    //   134: getfield second : Ljava/lang/Object;
    //   137: checkcast java/lang/Integer
    //   140: invokevirtual intValue : ()I
    //   143: iconst_m1
    //   144: if_icmpeq -> 197
    //   147: aload #13
    //   149: getfield first : Ljava/lang/Object;
    //   152: checkcast java/lang/Integer
    //   155: aload_0
    //   156: getfield second : Ljava/lang/Object;
    //   159: invokevirtual equals : (Ljava/lang/Object;)Z
    //   162: ifne -> 186
    //   165: iload #4
    //   167: istore_3
    //   168: aload #13
    //   170: getfield second : Ljava/lang/Object;
    //   173: checkcast java/lang/Integer
    //   176: aload_0
    //   177: getfield second : Ljava/lang/Object;
    //   180: invokevirtual equals : (Ljava/lang/Object;)Z
    //   183: ifeq -> 197
    //   186: aload_0
    //   187: getfield second : Ljava/lang/Object;
    //   190: checkcast java/lang/Integer
    //   193: invokevirtual intValue : ()I
    //   196: istore_3
    //   197: iload_2
    //   198: iconst_m1
    //   199: if_icmpne -> 219
    //   202: iload_3
    //   203: iconst_m1
    //   204: if_icmpne -> 219
    //   207: new android/util/Pair
    //   210: dup
    //   211: aload #11
    //   213: aload #10
    //   215: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   218: areturn
    //   219: iload_2
    //   220: iconst_m1
    //   221: if_icmpne -> 241
    //   224: new android/util/Pair
    //   227: dup
    //   228: iload_3
    //   229: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   232: aload #10
    //   234: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   237: astore_0
    //   238: goto -> 260
    //   241: iload_3
    //   242: iconst_m1
    //   243: if_icmpne -> 260
    //   246: new android/util/Pair
    //   249: dup
    //   250: iload_2
    //   251: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   254: aload #10
    //   256: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   259: astore_0
    //   260: iinc #1, 1
    //   263: goto -> 62
    //   266: aload_0
    //   267: areturn
    //   268: aload_0
    //   269: ldc_w '/'
    //   272: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   275: ifeq -> 410
    //   278: aload_0
    //   279: ldc_w '/'
    //   282: iconst_m1
    //   283: invokevirtual split : (Ljava/lang/String;I)[Ljava/lang/String;
    //   286: astore_0
    //   287: aload_0
    //   288: arraylength
    //   289: iconst_2
    //   290: if_icmpne -> 398
    //   293: aload_0
    //   294: iconst_0
    //   295: aaload
    //   296: invokestatic parseDouble : (Ljava/lang/String;)D
    //   299: d2l
    //   300: lstore #5
    //   302: aload_0
    //   303: iconst_1
    //   304: aaload
    //   305: invokestatic parseDouble : (Ljava/lang/String;)D
    //   308: d2l
    //   309: lstore #7
    //   311: lload #5
    //   313: lconst_0
    //   314: lcmp
    //   315: iflt -> 380
    //   318: lload #7
    //   320: lconst_0
    //   321: lcmp
    //   322: ifge -> 328
    //   325: goto -> 380
    //   328: lload #5
    //   330: ldc2_w 2147483647
    //   333: lcmp
    //   334: ifgt -> 366
    //   337: lload #7
    //   339: ldc2_w 2147483647
    //   342: lcmp
    //   343: ifle -> 349
    //   346: goto -> 366
    //   349: new android/util/Pair
    //   352: dup
    //   353: bipush #10
    //   355: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   358: iconst_5
    //   359: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   362: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   365: areturn
    //   366: new android/util/Pair
    //   369: dup
    //   370: iconst_5
    //   371: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   374: aload #10
    //   376: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   379: areturn
    //   380: new android/util/Pair
    //   383: dup
    //   384: bipush #10
    //   386: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   389: aload #10
    //   391: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   394: astore_0
    //   395: aload_0
    //   396: areturn
    //   397: astore_0
    //   398: new android/util/Pair
    //   401: dup
    //   402: aload #11
    //   404: aload #10
    //   406: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   409: areturn
    //   410: aload_0
    //   411: invokestatic parseLong : (Ljava/lang/String;)J
    //   414: invokestatic valueOf : (J)Ljava/lang/Long;
    //   417: astore #12
    //   419: aload #12
    //   421: invokevirtual longValue : ()J
    //   424: lconst_0
    //   425: lcmp
    //   426: iflt -> 457
    //   429: aload #12
    //   431: invokevirtual longValue : ()J
    //   434: ldc2_w 65535
    //   437: lcmp
    //   438: ifgt -> 457
    //   441: new android/util/Pair
    //   444: dup
    //   445: iconst_3
    //   446: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   449: iconst_4
    //   450: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   453: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   456: areturn
    //   457: aload #12
    //   459: invokevirtual longValue : ()J
    //   462: lconst_0
    //   463: lcmp
    //   464: ifge -> 482
    //   467: new android/util/Pair
    //   470: dup
    //   471: bipush #9
    //   473: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   476: aload #10
    //   478: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   481: areturn
    //   482: new android/util/Pair
    //   485: dup
    //   486: iconst_4
    //   487: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   490: aload #10
    //   492: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   495: astore #12
    //   497: aload #12
    //   499: areturn
    //   500: astore #12
    //   502: aload_0
    //   503: invokestatic parseDouble : (Ljava/lang/String;)D
    //   506: pop2
    //   507: new android/util/Pair
    //   510: dup
    //   511: bipush #12
    //   513: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   516: aload #10
    //   518: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   521: astore_0
    //   522: aload_0
    //   523: areturn
    //   524: astore_0
    //   525: new android/util/Pair
    //   528: dup
    //   529: aload #11
    //   531: aload #10
    //   533: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   536: areturn
    // Exception table:
    //   from	to	target	type
    //   293	311	397	java/lang/NumberFormatException
    //   349	366	397	java/lang/NumberFormatException
    //   366	380	397	java/lang/NumberFormatException
    //   380	395	397	java/lang/NumberFormatException
    //   410	457	500	java/lang/NumberFormatException
    //   457	482	500	java/lang/NumberFormatException
    //   482	497	500	java/lang/NumberFormatException
    //   502	522	524	java/lang/NumberFormatException
  }
  
  private void handleThumbnailFromJfif(ByteOrderedDataInputStream paramByteOrderedDataInputStream, HashMap paramHashMap) throws IOException {
    ExifAttribute exifAttribute2 = (ExifAttribute)paramHashMap.get("JPEGInterchangeFormat");
    ExifAttribute exifAttribute1 = (ExifAttribute)paramHashMap.get("JPEGInterchangeFormatLength");
    if (exifAttribute2 != null && exifAttribute1 != null) {
      int j = exifAttribute2.getIntValue(this.mExifByteOrder);
      int k = exifAttribute1.getIntValue(this.mExifByteOrder);
      int i = j;
      if (this.mMimeType == 7)
        i = j + this.mOrfMakerNoteOffset; 
      j = Math.min(k, paramByteOrderedDataInputStream.getLength() - i);
      if (i > 0 && j > 0) {
        this.mHasThumbnail = true;
        k = this.mExifOffset + i;
        this.mThumbnailOffset = k;
        this.mThumbnailLength = j;
        if (this.mFilename == null && this.mAssetInputStream == null && this.mSeekableFileDescriptor == null) {
          byte[] arrayOfByte = new byte[j];
          paramByteOrderedDataInputStream.seek(k);
          paramByteOrderedDataInputStream.readFully(arrayOfByte);
          this.mThumbnailBytes = arrayOfByte;
        } 
      } 
      if (DEBUG)
        Log.d("ExifInterface", "Setting thumbnail attributes with offset: " + i + ", length: " + j); 
    } 
  }
  
  private void handleThumbnailFromStrips(ByteOrderedDataInputStream paramByteOrderedDataInputStream, HashMap paramHashMap) throws IOException {
    ExifAttribute exifAttribute2 = (ExifAttribute)paramHashMap.get("StripOffsets");
    ExifAttribute exifAttribute1 = (ExifAttribute)paramHashMap.get("StripByteCounts");
    if (exifAttribute2 != null && exifAttribute1 != null) {
      long[] arrayOfLong2 = convertToLongArray(exifAttribute2.getValue(this.mExifByteOrder));
      long[] arrayOfLong1 = convertToLongArray(exifAttribute1.getValue(this.mExifByteOrder));
      if (arrayOfLong2 == null || arrayOfLong2.length == 0) {
        Log.w("ExifInterface", "stripOffsets should not be null or have zero length.");
        return;
      } 
      if (arrayOfLong1 == null || arrayOfLong1.length == 0) {
        Log.w("ExifInterface", "stripByteCounts should not be null or have zero length.");
        return;
      } 
      if (arrayOfLong2.length != arrayOfLong1.length) {
        Log.w("ExifInterface", "stripOffsets and stripByteCounts should have same length.");
        return;
      } 
      long l = 0L;
      int i = arrayOfLong1.length;
      byte b;
      for (b = 0; b < i; b++)
        l += arrayOfLong1[b]; 
      byte[] arrayOfByte = new byte[(int)l];
      int j = 0;
      i = 0;
      this.mAreThumbnailStripsConsecutive = true;
      this.mHasThumbnailStrips = true;
      this.mHasThumbnail = true;
      for (b = 0; b < arrayOfLong2.length; b++) {
        int m = (int)arrayOfLong2[b];
        int k = (int)arrayOfLong1[b];
        if (b < arrayOfLong2.length - 1 && (m + k) != arrayOfLong2[b + 1])
          this.mAreThumbnailStripsConsecutive = false; 
        m -= j;
        if (m < 0)
          Log.d("ExifInterface", "Invalid strip offset value"); 
        paramByteOrderedDataInputStream.seek(m);
        byte[] arrayOfByte1 = new byte[k];
        paramByteOrderedDataInputStream.read(arrayOfByte1);
        j = j + m + k;
        System.arraycopy(arrayOfByte1, 0, arrayOfByte, i, arrayOfByte1.length);
        i += arrayOfByte1.length;
      } 
      this.mThumbnailBytes = arrayOfByte;
      if (this.mAreThumbnailStripsConsecutive) {
        this.mThumbnailOffset = (int)arrayOfLong2[0] + this.mExifOffset;
        this.mThumbnailLength = arrayOfByte.length;
      } 
    } 
  }
  
  private void initForFilename(String paramString) throws IOException {
    if (paramString != null) {
      FileInputStream fileInputStream2 = null;
      this.mAssetInputStream = null;
      this.mFilename = paramString;
      FileInputStream fileInputStream1 = fileInputStream2;
      try {
        FileInputStream fileInputStream4 = new FileInputStream();
        fileInputStream1 = fileInputStream2;
        this(paramString);
        FileInputStream fileInputStream3 = fileInputStream4;
        fileInputStream1 = fileInputStream3;
        if (isSeekableFD(fileInputStream3.getFD())) {
          fileInputStream1 = fileInputStream3;
          this.mSeekableFileDescriptor = fileInputStream3.getFD();
        } else {
          fileInputStream1 = fileInputStream3;
          this.mSeekableFileDescriptor = null;
        } 
        fileInputStream1 = fileInputStream3;
        loadAttributes(fileInputStream3);
        return;
      } finally {
        closeQuietly(fileInputStream1);
      } 
    } 
    throw new NullPointerException("filename cannot be null");
  }
  
  private static boolean isExifDataOnly(BufferedInputStream paramBufferedInputStream) throws IOException {
    byte[] arrayOfByte = IDENTIFIER_EXIF_APP1;
    paramBufferedInputStream.mark(arrayOfByte.length);
    arrayOfByte = new byte[arrayOfByte.length];
    paramBufferedInputStream.read(arrayOfByte);
    paramBufferedInputStream.reset();
    byte b = 0;
    while (true) {
      byte[] arrayOfByte1 = IDENTIFIER_EXIF_APP1;
      if (b < arrayOfByte1.length) {
        if (arrayOfByte[b] != arrayOfByte1[b])
          return false; 
        b++;
        continue;
      } 
      return true;
    } 
  }
  
  private boolean isHeifFormat(byte[] paramArrayOfbyte) throws IOException {
    ByteOrderedDataInputStream byteOrderedDataInputStream3 = null;
    ByteOrderedDataInputStream byteOrderedDataInputStream4 = null;
    ByteOrderedDataInputStream byteOrderedDataInputStream2 = byteOrderedDataInputStream4;
    ByteOrderedDataInputStream byteOrderedDataInputStream1 = byteOrderedDataInputStream3;
    try {
      ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream();
      byteOrderedDataInputStream2 = byteOrderedDataInputStream4;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream3;
      this(paramArrayOfbyte);
      byteOrderedDataInputStream2 = byteOrderedDataInputStream;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream;
      long l3 = byteOrderedDataInputStream.readInt();
      byteOrderedDataInputStream2 = byteOrderedDataInputStream;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream;
      byte[] arrayOfByte = new byte[4];
      byteOrderedDataInputStream2 = byteOrderedDataInputStream;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream;
      byteOrderedDataInputStream.read(arrayOfByte);
      byteOrderedDataInputStream2 = byteOrderedDataInputStream;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream;
      boolean bool = Arrays.equals(arrayOfByte, HEIF_TYPE_FTYP);
      if (!bool) {
        byteOrderedDataInputStream.close();
        return false;
      } 
      long l2 = 8L;
      long l1 = l3;
      if (l3 == 1L) {
        byteOrderedDataInputStream2 = byteOrderedDataInputStream;
        byteOrderedDataInputStream1 = byteOrderedDataInputStream;
        l1 = byteOrderedDataInputStream.readLong();
        if (l1 < 16L) {
          byteOrderedDataInputStream.close();
          return false;
        } 
        l2 = 8L + 8L;
      } 
      l3 = l1;
      byteOrderedDataInputStream2 = byteOrderedDataInputStream;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream;
      if (l1 > paramArrayOfbyte.length) {
        byteOrderedDataInputStream2 = byteOrderedDataInputStream;
        byteOrderedDataInputStream1 = byteOrderedDataInputStream;
        int k = paramArrayOfbyte.length;
        l3 = k;
      } 
      l2 = l3 - l2;
      if (l2 < 8L) {
        byteOrderedDataInputStream.close();
        return false;
      } 
      byteOrderedDataInputStream2 = byteOrderedDataInputStream;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream;
      paramArrayOfbyte = new byte[4];
      int i = 0;
      int j = 0;
      l1 = 0L;
      while (true) {
        byteOrderedDataInputStream2 = byteOrderedDataInputStream;
        byteOrderedDataInputStream1 = byteOrderedDataInputStream;
        if (l1 < l2 / 4L) {
          byteOrderedDataInputStream2 = byteOrderedDataInputStream;
          byteOrderedDataInputStream1 = byteOrderedDataInputStream;
          int k = byteOrderedDataInputStream.read(paramArrayOfbyte);
          byteOrderedDataInputStream2 = byteOrderedDataInputStream;
          byteOrderedDataInputStream1 = byteOrderedDataInputStream;
          int m = paramArrayOfbyte.length;
          if (k != m) {
            byteOrderedDataInputStream.close();
            return false;
          } 
          if (l1 == 1L) {
            m = j;
          } else {
            byteOrderedDataInputStream2 = byteOrderedDataInputStream;
            byteOrderedDataInputStream1 = byteOrderedDataInputStream;
            if (Arrays.equals(paramArrayOfbyte, HEIF_BRAND_MIF1)) {
              k = 1;
            } else {
              byteOrderedDataInputStream2 = byteOrderedDataInputStream;
              byteOrderedDataInputStream1 = byteOrderedDataInputStream;
              bool = Arrays.equals(paramArrayOfbyte, HEIF_BRAND_HEIC);
              k = i;
              if (bool) {
                j = 1;
                k = i;
              } 
            } 
            i = k;
            m = j;
            if (k != 0) {
              i = k;
              m = j;
              if (j) {
                byteOrderedDataInputStream.close();
                return true;
              } 
            } 
          } 
          l1++;
          j = m;
          continue;
        } 
        byteOrderedDataInputStream1 = byteOrderedDataInputStream;
        byteOrderedDataInputStream1.close();
        return false;
      } 
    } catch (Exception exception) {
      byteOrderedDataInputStream2 = byteOrderedDataInputStream1;
      if (DEBUG) {
        byteOrderedDataInputStream2 = byteOrderedDataInputStream1;
        Log.d("ExifInterface", "Exception parsing HEIF file type box.", exception);
      } 
      if (byteOrderedDataInputStream1 != null) {
        byteOrderedDataInputStream1.close();
        return false;
      } 
    } finally {}
    return false;
  }
  
  private static boolean isJpegFormat(byte[] paramArrayOfbyte) throws IOException {
    byte b = 0;
    while (true) {
      byte[] arrayOfByte = JPEG_SIGNATURE;
      if (b < arrayOfByte.length) {
        if (paramArrayOfbyte[b] != arrayOfByte[b])
          return false; 
        b++;
        continue;
      } 
      return true;
    } 
  }
  
  private boolean isOrfFormat(byte[] paramArrayOfbyte) throws IOException {
    ByteOrderedDataInputStream byteOrderedDataInputStream3 = null;
    ByteOrderedDataInputStream byteOrderedDataInputStream4 = null;
    boolean bool = false;
    ByteOrderedDataInputStream byteOrderedDataInputStream1 = byteOrderedDataInputStream4;
    ByteOrderedDataInputStream byteOrderedDataInputStream2 = byteOrderedDataInputStream3;
    try {
      ByteOrderedDataInputStream byteOrderedDataInputStream6 = new ByteOrderedDataInputStream();
      byteOrderedDataInputStream1 = byteOrderedDataInputStream4;
      byteOrderedDataInputStream2 = byteOrderedDataInputStream3;
      this(paramArrayOfbyte);
      ByteOrderedDataInputStream byteOrderedDataInputStream5 = byteOrderedDataInputStream6;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream5;
      byteOrderedDataInputStream2 = byteOrderedDataInputStream5;
      ByteOrder byteOrder = readByteOrder(byteOrderedDataInputStream5);
      byteOrderedDataInputStream1 = byteOrderedDataInputStream5;
      byteOrderedDataInputStream2 = byteOrderedDataInputStream5;
      this.mExifByteOrder = byteOrder;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream5;
      byteOrderedDataInputStream2 = byteOrderedDataInputStream5;
      byteOrderedDataInputStream5.setByteOrder(byteOrder);
      byteOrderedDataInputStream1 = byteOrderedDataInputStream5;
      byteOrderedDataInputStream2 = byteOrderedDataInputStream5;
      short s = byteOrderedDataInputStream5.readShort();
      if (s == 20306 || s == 21330)
        bool = true; 
      return bool;
    } catch (Exception exception) {
      return false;
    } finally {
      if (byteOrderedDataInputStream1 != null)
        byteOrderedDataInputStream1.close(); 
    } 
  }
  
  private boolean isPngFormat(byte[] paramArrayOfbyte) throws IOException {
    byte b = 0;
    while (true) {
      byte[] arrayOfByte = PNG_SIGNATURE;
      if (b < arrayOfByte.length) {
        if (paramArrayOfbyte[b] != arrayOfByte[b])
          return false; 
        b++;
        continue;
      } 
      return true;
    } 
  }
  
  private boolean isRafFormat(byte[] paramArrayOfbyte) throws IOException {
    byte[] arrayOfByte = "FUJIFILMCCD-RAW".getBytes(Charset.defaultCharset());
    for (byte b = 0; b < arrayOfByte.length; b++) {
      if (paramArrayOfbyte[b] != arrayOfByte[b])
        return false; 
    } 
    return true;
  }
  
  private boolean isRw2Format(byte[] paramArrayOfbyte) throws IOException {
    ByteOrderedDataInputStream byteOrderedDataInputStream3 = null;
    ByteOrderedDataInputStream byteOrderedDataInputStream4 = null;
    boolean bool = false;
    ByteOrderedDataInputStream byteOrderedDataInputStream2 = byteOrderedDataInputStream4;
    ByteOrderedDataInputStream byteOrderedDataInputStream1 = byteOrderedDataInputStream3;
    try {
      ByteOrderedDataInputStream byteOrderedDataInputStream6 = new ByteOrderedDataInputStream();
      byteOrderedDataInputStream2 = byteOrderedDataInputStream4;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream3;
      this(paramArrayOfbyte);
      ByteOrderedDataInputStream byteOrderedDataInputStream5 = byteOrderedDataInputStream6;
      byteOrderedDataInputStream2 = byteOrderedDataInputStream5;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream5;
      ByteOrder byteOrder = readByteOrder(byteOrderedDataInputStream5);
      byteOrderedDataInputStream2 = byteOrderedDataInputStream5;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream5;
      this.mExifByteOrder = byteOrder;
      byteOrderedDataInputStream2 = byteOrderedDataInputStream5;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream5;
      byteOrderedDataInputStream5.setByteOrder(byteOrder);
      byteOrderedDataInputStream2 = byteOrderedDataInputStream5;
      byteOrderedDataInputStream1 = byteOrderedDataInputStream5;
      short s = byteOrderedDataInputStream5.readShort();
      if (s == 85)
        bool = true; 
      return bool;
    } catch (Exception exception) {
      return false;
    } finally {
      if (byteOrderedDataInputStream2 != null)
        byteOrderedDataInputStream2.close(); 
    } 
  }
  
  private static boolean isSeekableFD(FileDescriptor paramFileDescriptor) {
    if (Build.VERSION.SDK_INT >= 21)
      try {
        Os.lseek(paramFileDescriptor, 0L, OsConstants.SEEK_CUR);
        return true;
      } catch (Exception exception) {
        if (DEBUG)
          Log.d("ExifInterface", "The file descriptor for the given input is not seekable"); 
        return false;
      }  
    return false;
  }
  
  private boolean isSupportedDataType(HashMap paramHashMap) throws IOException {
    ExifAttribute exifAttribute = (ExifAttribute)paramHashMap.get("BitsPerSample");
    if (exifAttribute != null) {
      int[] arrayOfInt2 = (int[])exifAttribute.getValue(this.mExifByteOrder);
      int[] arrayOfInt1 = BITS_PER_SAMPLE_RGB;
      if (Arrays.equals(arrayOfInt1, arrayOfInt2))
        return true; 
      if (this.mMimeType == 3) {
        ExifAttribute exifAttribute1 = (ExifAttribute)paramHashMap.get("PhotometricInterpretation");
        if (exifAttribute1 != null) {
          int i = exifAttribute1.getIntValue(this.mExifByteOrder);
          if ((i == 1 && Arrays.equals(arrayOfInt2, BITS_PER_SAMPLE_GREYSCALE_2)) || (i == 6 && Arrays.equals(arrayOfInt2, arrayOfInt1)))
            return true; 
        } 
      } 
    } 
    if (DEBUG)
      Log.d("ExifInterface", "Unsupported data type value"); 
    return false;
  }
  
  private boolean isSupportedFormatForSavingAttributes() {
    if (this.mIsSupportedFile) {
      int i = this.mMimeType;
      if (i == 4 || i == 13 || i == 14)
        return true; 
    } 
    return false;
  }
  
  public static boolean isSupportedMimeType(String paramString) {
    if (paramString != null) {
      switch (paramString.toLowerCase(Locale.ROOT)) {
        default:
          return false;
        case "image/jpeg":
        case "image/x-adobe-dng":
        case "image/x-canon-cr2":
        case "image/x-nikon-nef":
        case "image/x-nikon-nrw":
        case "image/x-sony-arw":
        case "image/x-panasonic-rw2":
        case "image/x-olympus-orf":
        case "image/x-pentax-pef":
        case "image/x-samsung-srw":
        case "image/x-fuji-raf":
        case "image/heic":
        case "image/heif":
        case "image/png":
        case "image/webp":
          break;
      } 
      return true;
    } 
    throw new NullPointerException("mimeType shouldn't be null");
  }
  
  private boolean isThumbnail(HashMap paramHashMap) throws IOException {
    ExifAttribute exifAttribute2 = (ExifAttribute)paramHashMap.get("ImageLength");
    ExifAttribute exifAttribute1 = (ExifAttribute)paramHashMap.get("ImageWidth");
    if (exifAttribute2 != null && exifAttribute1 != null) {
      int j = exifAttribute2.getIntValue(this.mExifByteOrder);
      int i = exifAttribute1.getIntValue(this.mExifByteOrder);
      if (j <= 512 && i <= 512)
        return true; 
    } 
    return false;
  }
  
  private boolean isWebpFormat(byte[] paramArrayOfbyte) throws IOException {
    byte b = 0;
    while (true) {
      byte[] arrayOfByte = WEBP_SIGNATURE_1;
      if (b < arrayOfByte.length) {
        if (paramArrayOfbyte[b] != arrayOfByte[b])
          return false; 
        b++;
        continue;
      } 
      b = 0;
      while (true) {
        arrayOfByte = WEBP_SIGNATURE_2;
        if (b < arrayOfByte.length) {
          if (paramArrayOfbyte[WEBP_SIGNATURE_1.length + b + 4] != arrayOfByte[b])
            return false; 
          b++;
          continue;
        } 
        return true;
      } 
      break;
    } 
  }
  
  private void loadAttributes(InputStream paramInputStream) {
    if (paramInputStream != null) {
      byte b = 0;
      try {
        while (b < EXIF_TAGS.length) {
          this.mAttributes[b] = new HashMap<>();
          b++;
        } 
        InputStream inputStream = paramInputStream;
        if (!this.mIsExifDataOnly) {
          inputStream = new BufferedInputStream();
          super(paramInputStream, 5000);
          this.mMimeType = getMimeType((BufferedInputStream)inputStream);
        } 
        paramInputStream = new ByteOrderedDataInputStream();
        super(inputStream);
        if (!this.mIsExifDataOnly) {
          switch (this.mMimeType) {
            case 14:
              getWebpAttributes((ByteOrderedDataInputStream)paramInputStream);
              break;
            case 13:
              getPngAttributes((ByteOrderedDataInputStream)paramInputStream);
              break;
            case 12:
              getHeifAttributes((ByteOrderedDataInputStream)paramInputStream);
              break;
            case 10:
              getRw2Attributes((ByteOrderedDataInputStream)paramInputStream);
              break;
            case 9:
              getRafAttributes((ByteOrderedDataInputStream)paramInputStream);
              break;
            case 7:
              getOrfAttributes((ByteOrderedDataInputStream)paramInputStream);
              break;
            case 4:
              getJpegAttributes((ByteOrderedDataInputStream)paramInputStream, 0, 0);
              break;
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 8:
            case 11:
              getRawAttributes((ByteOrderedDataInputStream)paramInputStream);
              break;
          } 
        } else {
          getStandaloneAttributes((ByteOrderedDataInputStream)paramInputStream);
        } 
        setThumbnailData((ByteOrderedDataInputStream)paramInputStream);
        this.mIsSupportedFile = true;
        addDefaultValuesForCompatibility();
        if (DEBUG) {
          printAttributes();
          return;
        } 
      } catch (IOException iOException) {
        this.mIsSupportedFile = false;
        boolean bool = DEBUG;
        if (bool)
          Log.w("ExifInterface", "Invalid image: ExifInterface got an unsupported image format file(ExifInterface supports JPEG and some RAW image formats only) or a corrupted JPEG file to ExifInterface.", iOException); 
        addDefaultValuesForCompatibility();
        if (bool) {
          printAttributes();
          return;
        } 
      } finally {}
      return;
    } 
    throw new NullPointerException("inputstream shouldn't be null");
  }
  
  private static long parseDateTime(String paramString1, String paramString2) {
    if (paramString1 == null || !sNonZeroTimePattern.matcher(paramString1).matches())
      return -1L; 
    ParsePosition parsePosition = new ParsePosition(0);
    try {
      Date date = sFormatter.parse(paramString1, parsePosition);
      if (date == null)
        return -1L; 
      long l2 = date.getTime();
      long l1 = l2;
      if (paramString2 != null)
        try {
          for (l1 = Long.parseLong(paramString2); l1 > 1000L; l1 /= 10L);
          l1 = l2 + l1;
        } catch (NumberFormatException numberFormatException) {
          l1 = l2;
        }  
      return l1;
    } catch (IllegalArgumentException illegalArgumentException) {
      return -1L;
    } 
  }
  
  private void parseTiffHeaders(ByteOrderedDataInputStream paramByteOrderedDataInputStream, int paramInt) throws IOException {
    ByteOrder byteOrder = readByteOrder(paramByteOrderedDataInputStream);
    this.mExifByteOrder = byteOrder;
    paramByteOrderedDataInputStream.setByteOrder(byteOrder);
    int j = paramByteOrderedDataInputStream.readUnsignedShort();
    int i = this.mMimeType;
    if (i == 7 || i == 10 || j == 42) {
      i = paramByteOrderedDataInputStream.readInt();
      if (i >= 8 && i < paramInt) {
        paramInt = i - 8;
        if (paramInt <= 0 || paramByteOrderedDataInputStream.skipBytes(paramInt) == paramInt)
          return; 
        throw new IOException("Couldn't jump to first Ifd: " + paramInt);
      } 
      throw new IOException("Invalid first Ifd offset: " + i);
    } 
    throw new IOException("Invalid start code: " + Integer.toHexString(j));
  }
  
  private void printAttributes() {
    for (byte b = 0; b < this.mAttributes.length; b++) {
      Log.d("ExifInterface", "The size of tag group[" + b + "]: " + this.mAttributes[b].size());
      for (Map.Entry<String, ExifAttribute> entry : this.mAttributes[b].entrySet()) {
        ExifAttribute exifAttribute = (ExifAttribute)entry.getValue();
        Log.d("ExifInterface", "tagName: " + (String)entry.getKey() + ", tagType: " + exifAttribute.toString() + ", tagValue: '" + exifAttribute.getStringValue(this.mExifByteOrder) + "'");
      } 
    } 
  }
  
  private ByteOrder readByteOrder(ByteOrderedDataInputStream paramByteOrderedDataInputStream) throws IOException {
    short s = paramByteOrderedDataInputStream.readShort();
    switch (s) {
      default:
        throw new IOException("Invalid byte order: " + Integer.toHexString(s));
      case 19789:
        if (DEBUG)
          Log.d("ExifInterface", "readExifSegment: Byte Align MM"); 
        return ByteOrder.BIG_ENDIAN;
      case 18761:
        break;
    } 
    if (DEBUG)
      Log.d("ExifInterface", "readExifSegment: Byte Align II"); 
    return ByteOrder.LITTLE_ENDIAN;
  }
  
  private void readExifSegment(byte[] paramArrayOfbyte, int paramInt) throws IOException {
    ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(paramArrayOfbyte);
    parseTiffHeaders(byteOrderedDataInputStream, paramArrayOfbyte.length);
    readImageFileDirectory(byteOrderedDataInputStream, paramInt);
  }
  
  private void readImageFileDirectory(ByteOrderedDataInputStream paramByteOrderedDataInputStream, int paramInt) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield mAttributesOffsets : Ljava/util/Set;
    //   4: aload_1
    //   5: getfield mPosition : I
    //   8: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   11: invokeinterface add : (Ljava/lang/Object;)Z
    //   16: pop
    //   17: aload_1
    //   18: getfield mPosition : I
    //   21: iconst_2
    //   22: iadd
    //   23: aload_1
    //   24: getfield mLength : I
    //   27: if_icmple -> 31
    //   30: return
    //   31: aload_1
    //   32: invokevirtual readShort : ()S
    //   35: istore #5
    //   37: getstatic androidx/exifinterface/media/ExifInterface.DEBUG : Z
    //   40: ifeq -> 71
    //   43: ldc_w 'ExifInterface'
    //   46: new java/lang/StringBuilder
    //   49: dup
    //   50: invokespecial <init> : ()V
    //   53: ldc_w 'numberOfDirectoryEntry: '
    //   56: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: iload #5
    //   61: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   64: invokevirtual toString : ()Ljava/lang/String;
    //   67: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   70: pop
    //   71: aload_1
    //   72: getfield mPosition : I
    //   75: iload #5
    //   77: bipush #12
    //   79: imul
    //   80: iadd
    //   81: aload_1
    //   82: getfield mLength : I
    //   85: if_icmpgt -> 1553
    //   88: iload #5
    //   90: ifgt -> 96
    //   93: goto -> 1553
    //   96: iconst_0
    //   97: istore #4
    //   99: iload #4
    //   101: iload #5
    //   103: if_icmpge -> 1354
    //   106: aload_1
    //   107: invokevirtual readUnsignedShort : ()I
    //   110: istore #9
    //   112: aload_1
    //   113: invokevirtual readUnsignedShort : ()I
    //   116: istore #6
    //   118: aload_1
    //   119: invokevirtual readInt : ()I
    //   122: istore #8
    //   124: aload_1
    //   125: invokevirtual peek : ()I
    //   128: i2l
    //   129: ldc2_w 4
    //   132: ladd
    //   133: lstore #12
    //   135: getstatic androidx/exifinterface/media/ExifInterface.sExifTagMapsForReading : [Ljava/util/HashMap;
    //   138: iload_2
    //   139: aaload
    //   140: iload #9
    //   142: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   145: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   148: checkcast androidx/exifinterface/media/ExifInterface$ExifTag
    //   151: astore #16
    //   153: getstatic androidx/exifinterface/media/ExifInterface.DEBUG : Z
    //   156: istore #14
    //   158: iload #14
    //   160: ifeq -> 234
    //   163: aload #16
    //   165: ifnull -> 178
    //   168: aload #16
    //   170: getfield name : Ljava/lang/String;
    //   173: astore #15
    //   175: goto -> 181
    //   178: aconst_null
    //   179: astore #15
    //   181: ldc_w 'ExifInterface'
    //   184: ldc_w 'ifdType: %d, tagNumber: %d, tagName: %s, dataFormat: %d, numberOfComponents: %d'
    //   187: iconst_5
    //   188: anewarray java/lang/Object
    //   191: dup
    //   192: iconst_0
    //   193: iload_2
    //   194: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   197: aastore
    //   198: dup
    //   199: iconst_1
    //   200: iload #9
    //   202: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   205: aastore
    //   206: dup
    //   207: iconst_2
    //   208: aload #15
    //   210: aastore
    //   211: dup
    //   212: iconst_3
    //   213: iload #6
    //   215: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   218: aastore
    //   219: dup
    //   220: iconst_4
    //   221: iload #8
    //   223: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   226: aastore
    //   227: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   230: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   233: pop
    //   234: iconst_0
    //   235: istore #7
    //   237: aload #16
    //   239: ifnonnull -> 281
    //   242: iload #14
    //   244: ifeq -> 278
    //   247: ldc_w 'ExifInterface'
    //   250: new java/lang/StringBuilder
    //   253: dup
    //   254: invokespecial <init> : ()V
    //   257: ldc_w 'Skip the tag entry since tag number is not defined: '
    //   260: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   263: iload #9
    //   265: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   268: invokevirtual toString : ()Ljava/lang/String;
    //   271: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   274: pop
    //   275: goto -> 490
    //   278: goto -> 490
    //   281: iload #6
    //   283: ifle -> 457
    //   286: getstatic androidx/exifinterface/media/ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT : [I
    //   289: astore #15
    //   291: iload #6
    //   293: aload #15
    //   295: arraylength
    //   296: if_icmplt -> 302
    //   299: goto -> 457
    //   302: aload #16
    //   304: iload #6
    //   306: invokevirtual isFormatCompatible : (I)Z
    //   309: ifne -> 369
    //   312: iload #14
    //   314: ifeq -> 366
    //   317: ldc_w 'ExifInterface'
    //   320: new java/lang/StringBuilder
    //   323: dup
    //   324: invokespecial <init> : ()V
    //   327: ldc_w 'Skip the tag entry since data format ('
    //   330: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   333: getstatic androidx/exifinterface/media/ExifInterface.IFD_FORMAT_NAMES : [Ljava/lang/String;
    //   336: iload #6
    //   338: aaload
    //   339: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   342: ldc_w ') is unexpected for tag: '
    //   345: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   348: aload #16
    //   350: getfield name : Ljava/lang/String;
    //   353: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   356: invokevirtual toString : ()Ljava/lang/String;
    //   359: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   362: pop
    //   363: goto -> 490
    //   366: goto -> 490
    //   369: iload #6
    //   371: istore_3
    //   372: iload #6
    //   374: bipush #7
    //   376: if_icmpne -> 385
    //   379: aload #16
    //   381: getfield primaryFormat : I
    //   384: istore_3
    //   385: iload #8
    //   387: i2l
    //   388: aload #15
    //   390: iload_3
    //   391: iaload
    //   392: i2l
    //   393: lmul
    //   394: lstore #10
    //   396: lload #10
    //   398: lconst_0
    //   399: lcmp
    //   400: iflt -> 421
    //   403: lload #10
    //   405: ldc2_w 2147483647
    //   408: lcmp
    //   409: ifle -> 415
    //   412: goto -> 421
    //   415: iconst_1
    //   416: istore #7
    //   418: goto -> 496
    //   421: iload #14
    //   423: ifeq -> 454
    //   426: ldc_w 'ExifInterface'
    //   429: new java/lang/StringBuilder
    //   432: dup
    //   433: invokespecial <init> : ()V
    //   436: ldc_w 'Skip the tag entry since the number of components is invalid: '
    //   439: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   442: iload #8
    //   444: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   447: invokevirtual toString : ()Ljava/lang/String;
    //   450: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   453: pop
    //   454: goto -> 496
    //   457: iload #14
    //   459: ifeq -> 490
    //   462: ldc_w 'ExifInterface'
    //   465: new java/lang/StringBuilder
    //   468: dup
    //   469: invokespecial <init> : ()V
    //   472: ldc_w 'Skip the tag entry since data format is invalid: '
    //   475: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   478: iload #6
    //   480: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   483: invokevirtual toString : ()Ljava/lang/String;
    //   486: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   489: pop
    //   490: lconst_0
    //   491: lstore #10
    //   493: iload #6
    //   495: istore_3
    //   496: iload #7
    //   498: ifne -> 510
    //   501: aload_1
    //   502: lload #12
    //   504: invokevirtual seek : (J)V
    //   507: goto -> 1344
    //   510: lload #10
    //   512: ldc2_w 4
    //   515: lcmp
    //   516: ifle -> 816
    //   519: aload_1
    //   520: invokevirtual readInt : ()I
    //   523: istore #6
    //   525: iload #14
    //   527: ifeq -> 561
    //   530: ldc_w 'ExifInterface'
    //   533: new java/lang/StringBuilder
    //   536: dup
    //   537: invokespecial <init> : ()V
    //   540: ldc_w 'seek to data offset: '
    //   543: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   546: iload #6
    //   548: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   551: invokevirtual toString : ()Ljava/lang/String;
    //   554: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   557: pop
    //   558: goto -> 561
    //   561: aload_0
    //   562: getfield mMimeType : I
    //   565: istore #7
    //   567: iload #7
    //   569: bipush #7
    //   571: if_icmpne -> 722
    //   574: ldc_w 'MakerNote'
    //   577: aload #16
    //   579: getfield name : Ljava/lang/String;
    //   582: invokevirtual equals : (Ljava/lang/Object;)Z
    //   585: ifeq -> 597
    //   588: aload_0
    //   589: iload #6
    //   591: putfield mOrfMakerNoteOffset : I
    //   594: goto -> 749
    //   597: iload_2
    //   598: bipush #6
    //   600: if_icmpne -> 719
    //   603: ldc_w 'ThumbnailImage'
    //   606: aload #16
    //   608: getfield name : Ljava/lang/String;
    //   611: invokevirtual equals : (Ljava/lang/Object;)Z
    //   614: ifeq -> 716
    //   617: aload_0
    //   618: iload #6
    //   620: putfield mOrfThumbnailOffset : I
    //   623: aload_0
    //   624: iload #8
    //   626: putfield mOrfThumbnailLength : I
    //   629: bipush #6
    //   631: aload_0
    //   632: getfield mExifByteOrder : Ljava/nio/ByteOrder;
    //   635: invokestatic createUShort : (ILjava/nio/ByteOrder;)Landroidx/exifinterface/media/ExifInterface$ExifAttribute;
    //   638: astore #17
    //   640: aload_0
    //   641: getfield mOrfThumbnailOffset : I
    //   644: i2l
    //   645: aload_0
    //   646: getfield mExifByteOrder : Ljava/nio/ByteOrder;
    //   649: invokestatic createULong : (JLjava/nio/ByteOrder;)Landroidx/exifinterface/media/ExifInterface$ExifAttribute;
    //   652: astore #18
    //   654: aload_0
    //   655: getfield mOrfThumbnailLength : I
    //   658: i2l
    //   659: aload_0
    //   660: getfield mExifByteOrder : Ljava/nio/ByteOrder;
    //   663: invokestatic createULong : (JLjava/nio/ByteOrder;)Landroidx/exifinterface/media/ExifInterface$ExifAttribute;
    //   666: astore #15
    //   668: aload_0
    //   669: getfield mAttributes : [Ljava/util/HashMap;
    //   672: iconst_4
    //   673: aaload
    //   674: ldc_w 'Compression'
    //   677: aload #17
    //   679: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   682: pop
    //   683: aload_0
    //   684: getfield mAttributes : [Ljava/util/HashMap;
    //   687: iconst_4
    //   688: aaload
    //   689: ldc_w 'JPEGInterchangeFormat'
    //   692: aload #18
    //   694: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   697: pop
    //   698: aload_0
    //   699: getfield mAttributes : [Ljava/util/HashMap;
    //   702: iconst_4
    //   703: aaload
    //   704: ldc_w 'JPEGInterchangeFormatLength'
    //   707: aload #15
    //   709: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   712: pop
    //   713: goto -> 749
    //   716: goto -> 749
    //   719: goto -> 749
    //   722: iload #7
    //   724: bipush #10
    //   726: if_icmpne -> 749
    //   729: ldc_w 'JpgFromRaw'
    //   732: aload #16
    //   734: getfield name : Ljava/lang/String;
    //   737: invokevirtual equals : (Ljava/lang/Object;)Z
    //   740: ifeq -> 749
    //   743: aload_0
    //   744: iload #6
    //   746: putfield mRw2JpgFromRawOffset : I
    //   749: iload #6
    //   751: i2l
    //   752: lload #10
    //   754: ladd
    //   755: aload_1
    //   756: getfield mLength : I
    //   759: i2l
    //   760: lcmp
    //   761: ifgt -> 774
    //   764: aload_1
    //   765: iload #6
    //   767: i2l
    //   768: invokevirtual seek : (J)V
    //   771: goto -> 816
    //   774: iload #14
    //   776: ifeq -> 807
    //   779: ldc_w 'ExifInterface'
    //   782: new java/lang/StringBuilder
    //   785: dup
    //   786: invokespecial <init> : ()V
    //   789: ldc_w 'Skip the tag entry since data offset is invalid: '
    //   792: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   795: iload #6
    //   797: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   800: invokevirtual toString : ()Ljava/lang/String;
    //   803: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   806: pop
    //   807: aload_1
    //   808: lload #12
    //   810: invokevirtual seek : (J)V
    //   813: goto -> 1344
    //   816: getstatic androidx/exifinterface/media/ExifInterface.sExifPointerTagMap : Ljava/util/HashMap;
    //   819: iload #9
    //   821: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   824: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   827: checkcast java/lang/Integer
    //   830: astore #15
    //   832: iload #14
    //   834: ifeq -> 876
    //   837: ldc_w 'ExifInterface'
    //   840: new java/lang/StringBuilder
    //   843: dup
    //   844: invokespecial <init> : ()V
    //   847: ldc_w 'nextIfdType: '
    //   850: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   853: aload #15
    //   855: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   858: ldc_w ' byteCount: '
    //   861: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   864: lload #10
    //   866: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   869: invokevirtual toString : ()Ljava/lang/String;
    //   872: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   875: pop
    //   876: aload #15
    //   878: ifnull -> 1166
    //   881: ldc2_w -1
    //   884: lstore #10
    //   886: iload_3
    //   887: lookupswitch default -> 936, 3 -> 968, 4 -> 959, 8 -> 949, 9 -> 939, 13 -> 939
    //   936: goto -> 975
    //   939: aload_1
    //   940: invokevirtual readInt : ()I
    //   943: i2l
    //   944: lstore #10
    //   946: goto -> 975
    //   949: aload_1
    //   950: invokevirtual readShort : ()S
    //   953: i2l
    //   954: lstore #10
    //   956: goto -> 975
    //   959: aload_1
    //   960: invokevirtual readUnsignedInt : ()J
    //   963: lstore #10
    //   965: goto -> 975
    //   968: aload_1
    //   969: invokevirtual readUnsignedShort : ()I
    //   972: i2l
    //   973: lstore #10
    //   975: iload #14
    //   977: ifeq -> 1016
    //   980: ldc_w 'ExifInterface'
    //   983: ldc_w 'Offset: %d, tagName: %s'
    //   986: iconst_2
    //   987: anewarray java/lang/Object
    //   990: dup
    //   991: iconst_0
    //   992: lload #10
    //   994: invokestatic valueOf : (J)Ljava/lang/Long;
    //   997: aastore
    //   998: dup
    //   999: iconst_1
    //   1000: aload #16
    //   1002: getfield name : Ljava/lang/String;
    //   1005: aastore
    //   1006: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1009: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   1012: pop
    //   1013: goto -> 1016
    //   1016: lload #10
    //   1018: lconst_0
    //   1019: lcmp
    //   1020: ifle -> 1124
    //   1023: lload #10
    //   1025: aload_1
    //   1026: getfield mLength : I
    //   1029: i2l
    //   1030: lcmp
    //   1031: ifge -> 1124
    //   1034: aload_0
    //   1035: getfield mAttributesOffsets : Ljava/util/Set;
    //   1038: lload #10
    //   1040: l2i
    //   1041: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1044: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1049: ifne -> 1071
    //   1052: aload_1
    //   1053: lload #10
    //   1055: invokevirtual seek : (J)V
    //   1058: aload_0
    //   1059: aload_1
    //   1060: aload #15
    //   1062: invokevirtual intValue : ()I
    //   1065: invokespecial readImageFileDirectory : (Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream;I)V
    //   1068: goto -> 1157
    //   1071: iload #14
    //   1073: ifeq -> 1157
    //   1076: ldc_w 'ExifInterface'
    //   1079: new java/lang/StringBuilder
    //   1082: dup
    //   1083: invokespecial <init> : ()V
    //   1086: ldc_w 'Skip jump into the IFD since it has already been read: IfdType '
    //   1089: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1092: aload #15
    //   1094: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1097: ldc_w ' (at '
    //   1100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1103: lload #10
    //   1105: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   1108: ldc_w ')'
    //   1111: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1114: invokevirtual toString : ()Ljava/lang/String;
    //   1117: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   1120: pop
    //   1121: goto -> 1157
    //   1124: iload #14
    //   1126: ifeq -> 1157
    //   1129: ldc_w 'ExifInterface'
    //   1132: new java/lang/StringBuilder
    //   1135: dup
    //   1136: invokespecial <init> : ()V
    //   1139: ldc_w 'Skip jump into the IFD since its offset is invalid: '
    //   1142: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1145: lload #10
    //   1147: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   1150: invokevirtual toString : ()Ljava/lang/String;
    //   1153: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   1156: pop
    //   1157: aload_1
    //   1158: lload #12
    //   1160: invokevirtual seek : (J)V
    //   1163: goto -> 1344
    //   1166: aload_1
    //   1167: invokevirtual peek : ()I
    //   1170: istore #6
    //   1172: aload_0
    //   1173: getfield mExifOffset : I
    //   1176: istore #7
    //   1178: lload #10
    //   1180: l2i
    //   1181: newarray byte
    //   1183: astore #15
    //   1185: aload_1
    //   1186: aload #15
    //   1188: invokevirtual readFully : ([B)V
    //   1191: new androidx/exifinterface/media/ExifInterface$ExifAttribute
    //   1194: dup
    //   1195: iload_3
    //   1196: iload #8
    //   1198: iload #6
    //   1200: iload #7
    //   1202: iadd
    //   1203: i2l
    //   1204: aload #15
    //   1206: invokespecial <init> : (IIJ[B)V
    //   1209: astore #15
    //   1211: aload_0
    //   1212: getfield mAttributes : [Ljava/util/HashMap;
    //   1215: iload_2
    //   1216: aaload
    //   1217: aload #16
    //   1219: getfield name : Ljava/lang/String;
    //   1222: aload #15
    //   1224: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1227: pop
    //   1228: ldc_w 'DNGVersion'
    //   1231: aload #16
    //   1233: getfield name : Ljava/lang/String;
    //   1236: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1239: ifeq -> 1247
    //   1242: aload_0
    //   1243: iconst_3
    //   1244: putfield mMimeType : I
    //   1247: ldc_w 'Make'
    //   1250: aload #16
    //   1252: getfield name : Ljava/lang/String;
    //   1255: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1258: ifne -> 1275
    //   1261: ldc_w 'Model'
    //   1264: aload #16
    //   1266: getfield name : Ljava/lang/String;
    //   1269: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1272: ifeq -> 1293
    //   1275: aload #15
    //   1277: aload_0
    //   1278: getfield mExifByteOrder : Ljava/nio/ByteOrder;
    //   1281: invokevirtual getStringValue : (Ljava/nio/ByteOrder;)Ljava/lang/String;
    //   1284: ldc_w 'PENTAX'
    //   1287: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   1290: ifne -> 1321
    //   1293: ldc_w 'Compression'
    //   1296: aload #16
    //   1298: getfield name : Ljava/lang/String;
    //   1301: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1304: ifeq -> 1327
    //   1307: aload #15
    //   1309: aload_0
    //   1310: getfield mExifByteOrder : Ljava/nio/ByteOrder;
    //   1313: invokevirtual getIntValue : (Ljava/nio/ByteOrder;)I
    //   1316: ldc 65535
    //   1318: if_icmpne -> 1327
    //   1321: aload_0
    //   1322: bipush #8
    //   1324: putfield mMimeType : I
    //   1327: aload_1
    //   1328: invokevirtual peek : ()I
    //   1331: i2l
    //   1332: lload #12
    //   1334: lcmp
    //   1335: ifeq -> 1344
    //   1338: aload_1
    //   1339: lload #12
    //   1341: invokevirtual seek : (J)V
    //   1344: iload #4
    //   1346: iconst_1
    //   1347: iadd
    //   1348: i2s
    //   1349: istore #4
    //   1351: goto -> 99
    //   1354: aload_1
    //   1355: invokevirtual peek : ()I
    //   1358: iconst_4
    //   1359: iadd
    //   1360: aload_1
    //   1361: getfield mLength : I
    //   1364: if_icmpgt -> 1552
    //   1367: aload_1
    //   1368: invokevirtual readInt : ()I
    //   1371: istore_2
    //   1372: getstatic androidx/exifinterface/media/ExifInterface.DEBUG : Z
    //   1375: istore #14
    //   1377: iload #14
    //   1379: ifeq -> 1406
    //   1382: ldc_w 'ExifInterface'
    //   1385: ldc_w 'nextIfdOffset: %d'
    //   1388: iconst_1
    //   1389: anewarray java/lang/Object
    //   1392: dup
    //   1393: iconst_0
    //   1394: iload_2
    //   1395: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1398: aastore
    //   1399: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1402: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   1405: pop
    //   1406: iload_2
    //   1407: i2l
    //   1408: lconst_0
    //   1409: lcmp
    //   1410: ifle -> 1520
    //   1413: iload_2
    //   1414: aload_1
    //   1415: getfield mLength : I
    //   1418: if_icmpge -> 1520
    //   1421: aload_0
    //   1422: getfield mAttributesOffsets : Ljava/util/Set;
    //   1425: iload_2
    //   1426: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1429: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1434: ifne -> 1485
    //   1437: aload_1
    //   1438: iload_2
    //   1439: i2l
    //   1440: invokevirtual seek : (J)V
    //   1443: aload_0
    //   1444: getfield mAttributes : [Ljava/util/HashMap;
    //   1447: iconst_4
    //   1448: aaload
    //   1449: invokevirtual isEmpty : ()Z
    //   1452: ifeq -> 1464
    //   1455: aload_0
    //   1456: aload_1
    //   1457: iconst_4
    //   1458: invokespecial readImageFileDirectory : (Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream;I)V
    //   1461: goto -> 1552
    //   1464: aload_0
    //   1465: getfield mAttributes : [Ljava/util/HashMap;
    //   1468: iconst_5
    //   1469: aaload
    //   1470: invokevirtual isEmpty : ()Z
    //   1473: ifeq -> 1552
    //   1476: aload_0
    //   1477: aload_1
    //   1478: iconst_5
    //   1479: invokespecial readImageFileDirectory : (Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream;I)V
    //   1482: goto -> 1552
    //   1485: iload #14
    //   1487: ifeq -> 1552
    //   1490: ldc_w 'ExifInterface'
    //   1493: new java/lang/StringBuilder
    //   1496: dup
    //   1497: invokespecial <init> : ()V
    //   1500: ldc_w 'Stop reading file since re-reading an IFD may cause an infinite loop: '
    //   1503: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1506: iload_2
    //   1507: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1510: invokevirtual toString : ()Ljava/lang/String;
    //   1513: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   1516: pop
    //   1517: goto -> 1552
    //   1520: iload #14
    //   1522: ifeq -> 1552
    //   1525: ldc_w 'ExifInterface'
    //   1528: new java/lang/StringBuilder
    //   1531: dup
    //   1532: invokespecial <init> : ()V
    //   1535: ldc_w 'Stop reading file since a wrong offset may cause an infinite loop: '
    //   1538: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1541: iload_2
    //   1542: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1545: invokevirtual toString : ()Ljava/lang/String;
    //   1548: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   1551: pop
    //   1552: return
    //   1553: return
  }
  
  private void removeAttribute(String paramString) {
    for (byte b = 0; b < EXIF_TAGS.length; b++)
      this.mAttributes[b].remove(paramString); 
  }
  
  private void retrieveJpegImageSize(ByteOrderedDataInputStream paramByteOrderedDataInputStream, int paramInt) throws IOException {
    ExifAttribute exifAttribute1 = this.mAttributes[paramInt].get("ImageLength");
    ExifAttribute exifAttribute2 = this.mAttributes[paramInt].get("ImageWidth");
    if (exifAttribute1 == null || exifAttribute2 == null) {
      exifAttribute1 = this.mAttributes[paramInt].get("JPEGInterchangeFormat");
      if (exifAttribute1 != null)
        getJpegAttributes(paramByteOrderedDataInputStream, exifAttribute1.getIntValue(this.mExifByteOrder), paramInt); 
    } 
  }
  
  private void saveJpegAttributes(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException {
    if (DEBUG)
      Log.d("ExifInterface", "saveJpegAttributes starting with (inputStream: " + paramInputStream + ", outputStream: " + paramOutputStream + ")"); 
    DataInputStream dataInputStream = new DataInputStream(paramInputStream);
    ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream(paramOutputStream, ByteOrder.BIG_ENDIAN);
    if (dataInputStream.readByte() == -1) {
      byteOrderedDataOutputStream.writeByte(-1);
      if (dataInputStream.readByte() == -40) {
        ExifAttribute exifAttribute;
        byteOrderedDataOutputStream.writeByte(-40);
        paramOutputStream = null;
        OutputStream outputStream = paramOutputStream;
        if (getAttribute("Xmp") != null) {
          outputStream = paramOutputStream;
          if (this.mXmpIsFromSeparateMarker)
            exifAttribute = this.mAttributes[0].remove("Xmp"); 
        } 
        byteOrderedDataOutputStream.writeByte(-1);
        byteOrderedDataOutputStream.writeByte(-31);
        writeExifSegment(byteOrderedDataOutputStream);
        if (exifAttribute != null)
          this.mAttributes[0].put("Xmp", exifAttribute); 
        byte[] arrayOfByte = new byte[4096];
        while (dataInputStream.readByte() == -1) {
          int i;
          int j;
          byte b = dataInputStream.readByte();
          switch (b) {
            default:
              byteOrderedDataOutputStream.writeByte(-1);
              byteOrderedDataOutputStream.writeByte(b);
              i = dataInputStream.readUnsignedShort();
              byteOrderedDataOutputStream.writeUnsignedShort(i);
              i -= 2;
              if (i >= 0)
                break; 
              throw new IOException("Invalid length");
            case -31:
              j = dataInputStream.readUnsignedShort() - 2;
              if (j >= 0) {
                byte[] arrayOfByte1 = new byte[6];
                if (j >= 6)
                  if (dataInputStream.read(arrayOfByte1) == 6) {
                    if (Arrays.equals(arrayOfByte1, IDENTIFIER_EXIF_APP1)) {
                      if (dataInputStream.skipBytes(j - 6) == j - 6)
                        continue; 
                      throw new IOException("Invalid length");
                    } 
                  } else {
                    throw new IOException("Invalid exif");
                  }  
                byteOrderedDataOutputStream.writeByte(-1);
                byteOrderedDataOutputStream.writeByte(i);
                byteOrderedDataOutputStream.writeUnsignedShort(j + 2);
                i = j;
                if (j >= 6) {
                  i = j - 6;
                  byteOrderedDataOutputStream.write(arrayOfByte1);
                } 
                while (i > 0) {
                  j = dataInputStream.read(arrayOfByte, 0, Math.min(i, arrayOfByte.length));
                  if (j >= 0) {
                    byteOrderedDataOutputStream.write(arrayOfByte, 0, j);
                    i -= j;
                  } 
                } 
                continue;
              } 
              throw new IOException("Invalid length");
            case -39:
            case -38:
              byteOrderedDataOutputStream.writeByte(-1);
              byteOrderedDataOutputStream.writeByte(i);
              copy(dataInputStream, byteOrderedDataOutputStream);
              return;
          } 
          while (i > 0) {
            j = dataInputStream.read(arrayOfByte, 0, Math.min(i, arrayOfByte.length));
            if (j >= 0) {
              byteOrderedDataOutputStream.write(arrayOfByte, 0, j);
              i -= j;
            } 
          } 
        } 
        throw new IOException("Invalid marker");
      } 
      throw new IOException("Invalid marker");
    } 
    throw new IOException("Invalid marker");
  }
  
  private void savePngAttributes(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException {
    OutputStream outputStream;
    if (DEBUG)
      Log.d("ExifInterface", "savePngAttributes starting with (inputStream: " + paramInputStream + ", outputStream: " + paramOutputStream + ")"); 
    DataInputStream dataInputStream = new DataInputStream(paramInputStream);
    ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream(paramOutputStream, ByteOrder.BIG_ENDIAN);
    byte[] arrayOfByte1 = PNG_SIGNATURE;
    copy(dataInputStream, byteOrderedDataOutputStream, arrayOfByte1.length);
    int i = this.mExifOffset;
    if (i == 0) {
      i = dataInputStream.readInt();
      byteOrderedDataOutputStream.writeInt(i);
      copy(dataInputStream, byteOrderedDataOutputStream, i + 4 + 4);
    } else {
      copy(dataInputStream, byteOrderedDataOutputStream, i - arrayOfByte1.length - 4 - 4);
      dataInputStream.skipBytes(dataInputStream.readInt() + 4 + 4);
    } 
    byte[] arrayOfByte2 = null;
    arrayOfByte1 = arrayOfByte2;
    try {
      paramOutputStream = new ByteArrayOutputStream();
      arrayOfByte1 = arrayOfByte2;
      super();
      outputStream = paramOutputStream;
      ByteOrderedDataOutputStream byteOrderedDataOutputStream1 = new ByteOrderedDataOutputStream();
      outputStream = paramOutputStream;
      this(paramOutputStream, ByteOrder.BIG_ENDIAN);
      outputStream = paramOutputStream;
      writeExifSegment(byteOrderedDataOutputStream1);
      outputStream = paramOutputStream;
      byte[] arrayOfByte = ((ByteArrayOutputStream)byteOrderedDataOutputStream1.mOutputStream).toByteArray();
      outputStream = paramOutputStream;
      byteOrderedDataOutputStream.write(arrayOfByte);
      outputStream = paramOutputStream;
      CRC32 cRC32 = new CRC32();
      outputStream = paramOutputStream;
      this();
      outputStream = paramOutputStream;
      cRC32.update(arrayOfByte, 4, arrayOfByte.length - 4);
      outputStream = paramOutputStream;
      byteOrderedDataOutputStream.writeInt((int)cRC32.getValue());
      closeQuietly(paramOutputStream);
      return;
    } finally {
      closeQuietly(outputStream);
    } 
  }
  
  private void saveWebpAttributes(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException {
    // Byte code:
    //   0: getstatic androidx/exifinterface/media/ExifInterface.DEBUG : Z
    //   3: ifeq -> 49
    //   6: ldc_w 'ExifInterface'
    //   9: new java/lang/StringBuilder
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: ldc_w 'saveWebpAttributes starting with (inputStream: '
    //   19: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: aload_1
    //   23: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   26: ldc_w ', outputStream: '
    //   29: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   32: aload_2
    //   33: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   36: ldc_w ')'
    //   39: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   42: invokevirtual toString : ()Ljava/lang/String;
    //   45: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   48: pop
    //   49: new androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream
    //   52: dup
    //   53: aload_1
    //   54: getstatic java/nio/ByteOrder.LITTLE_ENDIAN : Ljava/nio/ByteOrder;
    //   57: invokespecial <init> : (Ljava/io/InputStream;Ljava/nio/ByteOrder;)V
    //   60: astore #11
    //   62: new androidx/exifinterface/media/ExifInterface$ByteOrderedDataOutputStream
    //   65: dup
    //   66: aload_2
    //   67: getstatic java/nio/ByteOrder.LITTLE_ENDIAN : Ljava/nio/ByteOrder;
    //   70: invokespecial <init> : (Ljava/io/OutputStream;Ljava/nio/ByteOrder;)V
    //   73: astore #10
    //   75: getstatic androidx/exifinterface/media/ExifInterface.WEBP_SIGNATURE_1 : [B
    //   78: astore #12
    //   80: aload #11
    //   82: aload #10
    //   84: aload #12
    //   86: arraylength
    //   87: invokestatic copy : (Ljava/io/InputStream;Ljava/io/OutputStream;I)V
    //   90: getstatic androidx/exifinterface/media/ExifInterface.WEBP_SIGNATURE_2 : [B
    //   93: astore #13
    //   95: aload #11
    //   97: aload #13
    //   99: arraylength
    //   100: iconst_4
    //   101: iadd
    //   102: invokevirtual skipBytes : (I)I
    //   105: pop
    //   106: aconst_null
    //   107: astore #9
    //   109: aconst_null
    //   110: astore #7
    //   112: aload #7
    //   114: astore_2
    //   115: aload #9
    //   117: astore #6
    //   119: new java/io/ByteArrayOutputStream
    //   122: astore #8
    //   124: aload #7
    //   126: astore_2
    //   127: aload #9
    //   129: astore #6
    //   131: aload #8
    //   133: invokespecial <init> : ()V
    //   136: aload #8
    //   138: astore #7
    //   140: aload #7
    //   142: astore_2
    //   143: aload #7
    //   145: astore #6
    //   147: new androidx/exifinterface/media/ExifInterface$ByteOrderedDataOutputStream
    //   150: astore #8
    //   152: aload #7
    //   154: astore_2
    //   155: aload #7
    //   157: astore #6
    //   159: aload #8
    //   161: aload #7
    //   163: getstatic java/nio/ByteOrder.LITTLE_ENDIAN : Ljava/nio/ByteOrder;
    //   166: invokespecial <init> : (Ljava/io/OutputStream;Ljava/nio/ByteOrder;)V
    //   169: aload #7
    //   171: astore_2
    //   172: aload #7
    //   174: astore #6
    //   176: aload_0
    //   177: getfield mExifOffset : I
    //   180: istore_3
    //   181: iload_3
    //   182: ifeq -> 263
    //   185: aload #7
    //   187: astore_2
    //   188: aload #7
    //   190: astore #6
    //   192: aload #11
    //   194: aload #8
    //   196: iload_3
    //   197: aload #12
    //   199: arraylength
    //   200: iconst_4
    //   201: iadd
    //   202: aload #13
    //   204: arraylength
    //   205: iadd
    //   206: isub
    //   207: iconst_4
    //   208: isub
    //   209: iconst_4
    //   210: isub
    //   211: invokestatic copy : (Ljava/io/InputStream;Ljava/io/OutputStream;I)V
    //   214: aload #7
    //   216: astore_2
    //   217: aload #7
    //   219: astore #6
    //   221: aload #11
    //   223: iconst_4
    //   224: invokevirtual skipBytes : (I)I
    //   227: pop
    //   228: aload #7
    //   230: astore_2
    //   231: aload #7
    //   233: astore #6
    //   235: aload #11
    //   237: aload #11
    //   239: invokevirtual readInt : ()I
    //   242: invokevirtual skipBytes : (I)I
    //   245: pop
    //   246: aload #7
    //   248: astore_2
    //   249: aload #7
    //   251: astore #6
    //   253: aload_0
    //   254: aload #8
    //   256: invokespecial writeExifSegment : (Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataOutputStream;)I
    //   259: pop
    //   260: goto -> 639
    //   263: aload #7
    //   265: astore_2
    //   266: aload #7
    //   268: astore #6
    //   270: iconst_4
    //   271: newarray byte
    //   273: astore #12
    //   275: aload #7
    //   277: astore_2
    //   278: aload #7
    //   280: astore #6
    //   282: aload #11
    //   284: aload #12
    //   286: invokevirtual read : ([B)I
    //   289: aload #12
    //   291: arraylength
    //   292: if_icmpne -> 761
    //   295: aload #7
    //   297: astore_2
    //   298: aload #7
    //   300: astore #6
    //   302: getstatic androidx/exifinterface/media/ExifInterface.WEBP_CHUNK_TYPE_VP8X : [B
    //   305: astore #9
    //   307: aload #7
    //   309: astore_2
    //   310: aload #7
    //   312: astore #6
    //   314: aload #12
    //   316: aload #9
    //   318: invokestatic equals : ([B[B)Z
    //   321: ifeq -> 603
    //   324: aload #7
    //   326: astore_2
    //   327: aload #7
    //   329: astore #6
    //   331: aload #11
    //   333: invokevirtual readInt : ()I
    //   336: istore #4
    //   338: iconst_1
    //   339: istore #5
    //   341: iload #4
    //   343: iconst_2
    //   344: irem
    //   345: iconst_1
    //   346: if_icmpne -> 357
    //   349: iload #4
    //   351: iconst_1
    //   352: iadd
    //   353: istore_3
    //   354: goto -> 360
    //   357: iload #4
    //   359: istore_3
    //   360: aload #7
    //   362: astore_2
    //   363: aload #7
    //   365: astore #6
    //   367: iload_3
    //   368: newarray byte
    //   370: astore #12
    //   372: aload #7
    //   374: astore_2
    //   375: aload #7
    //   377: astore #6
    //   379: aload #11
    //   381: aload #12
    //   383: invokevirtual read : ([B)I
    //   386: pop
    //   387: aload #12
    //   389: iconst_0
    //   390: aload #12
    //   392: iconst_0
    //   393: baload
    //   394: bipush #8
    //   396: ior
    //   397: i2b
    //   398: bastore
    //   399: aload #12
    //   401: iconst_0
    //   402: baload
    //   403: iconst_1
    //   404: ishr
    //   405: iconst_1
    //   406: iand
    //   407: iconst_1
    //   408: if_icmpne -> 417
    //   411: iload #5
    //   413: istore_3
    //   414: goto -> 419
    //   417: iconst_0
    //   418: istore_3
    //   419: aload #7
    //   421: astore_2
    //   422: aload #7
    //   424: astore #6
    //   426: aload #8
    //   428: aload #9
    //   430: invokevirtual write : ([B)V
    //   433: aload #7
    //   435: astore_2
    //   436: aload #7
    //   438: astore #6
    //   440: aload #8
    //   442: iload #4
    //   444: invokevirtual writeInt : (I)V
    //   447: aload #7
    //   449: astore_2
    //   450: aload #7
    //   452: astore #6
    //   454: aload #8
    //   456: aload #12
    //   458: invokevirtual write : ([B)V
    //   461: iload_3
    //   462: ifeq -> 565
    //   465: aload #7
    //   467: astore_2
    //   468: aload #7
    //   470: astore #6
    //   472: aload_0
    //   473: aload #11
    //   475: aload #8
    //   477: getstatic androidx/exifinterface/media/ExifInterface.WEBP_CHUNK_TYPE_ANIM : [B
    //   480: aconst_null
    //   481: invokespecial copyChunksUpToGivenChunkType : (Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream;Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataOutputStream;[B[B)V
    //   484: aload #7
    //   486: astore_2
    //   487: aload #7
    //   489: astore #6
    //   491: iconst_4
    //   492: newarray byte
    //   494: astore #9
    //   496: aload #7
    //   498: astore_2
    //   499: aload #7
    //   501: astore #6
    //   503: aload_1
    //   504: aload #9
    //   506: invokevirtual read : ([B)I
    //   509: pop
    //   510: aload #7
    //   512: astore_2
    //   513: aload #7
    //   515: astore #6
    //   517: aload #9
    //   519: getstatic androidx/exifinterface/media/ExifInterface.WEBP_CHUNK_TYPE_ANMF : [B
    //   522: invokestatic equals : ([B[B)Z
    //   525: ifne -> 545
    //   528: aload #7
    //   530: astore_2
    //   531: aload #7
    //   533: astore #6
    //   535: aload_0
    //   536: aload #8
    //   538: invokespecial writeExifSegment : (Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataOutputStream;)I
    //   541: pop
    //   542: goto -> 600
    //   545: aload #7
    //   547: astore_2
    //   548: aload #7
    //   550: astore #6
    //   552: aload_0
    //   553: aload #11
    //   555: aload #8
    //   557: aload #9
    //   559: invokespecial copyWebPChunk : (Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream;Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataOutputStream;[B)V
    //   562: goto -> 484
    //   565: aload #7
    //   567: astore_2
    //   568: aload #7
    //   570: astore #6
    //   572: aload_0
    //   573: aload #11
    //   575: aload #8
    //   577: getstatic androidx/exifinterface/media/ExifInterface.WEBP_CHUNK_TYPE_VP8 : [B
    //   580: getstatic androidx/exifinterface/media/ExifInterface.WEBP_CHUNK_TYPE_VP8L : [B
    //   583: invokespecial copyChunksUpToGivenChunkType : (Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream;Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataOutputStream;[B[B)V
    //   586: aload #7
    //   588: astore_2
    //   589: aload #7
    //   591: astore #6
    //   593: aload_0
    //   594: aload #8
    //   596: invokespecial writeExifSegment : (Landroidx/exifinterface/media/ExifInterface$ByteOrderedDataOutputStream;)I
    //   599: pop
    //   600: goto -> 639
    //   603: aload #7
    //   605: astore_2
    //   606: aload #7
    //   608: astore #6
    //   610: aload #12
    //   612: getstatic androidx/exifinterface/media/ExifInterface.WEBP_CHUNK_TYPE_VP8 : [B
    //   615: invokestatic equals : ([B[B)Z
    //   618: ifne -> 727
    //   621: aload #7
    //   623: astore_2
    //   624: aload #7
    //   626: astore #6
    //   628: aload #12
    //   630: getstatic androidx/exifinterface/media/ExifInterface.WEBP_CHUNK_TYPE_VP8L : [B
    //   633: invokestatic equals : ([B[B)Z
    //   636: ifne -> 727
    //   639: aload #7
    //   641: astore_2
    //   642: aload #7
    //   644: astore #6
    //   646: aload #11
    //   648: aload #8
    //   650: invokestatic copy : (Ljava/io/InputStream;Ljava/io/OutputStream;)I
    //   653: pop
    //   654: aload #7
    //   656: astore_2
    //   657: aload #7
    //   659: astore #6
    //   661: aload #7
    //   663: invokevirtual size : ()I
    //   666: istore_3
    //   667: aload #7
    //   669: astore_2
    //   670: aload #7
    //   672: astore #6
    //   674: getstatic androidx/exifinterface/media/ExifInterface.WEBP_SIGNATURE_2 : [B
    //   677: astore_1
    //   678: aload #7
    //   680: astore_2
    //   681: aload #7
    //   683: astore #6
    //   685: aload #10
    //   687: iload_3
    //   688: aload_1
    //   689: arraylength
    //   690: iadd
    //   691: invokevirtual writeInt : (I)V
    //   694: aload #7
    //   696: astore_2
    //   697: aload #7
    //   699: astore #6
    //   701: aload #10
    //   703: aload_1
    //   704: invokevirtual write : ([B)V
    //   707: aload #7
    //   709: astore_2
    //   710: aload #7
    //   712: astore #6
    //   714: aload #7
    //   716: aload #10
    //   718: invokevirtual writeTo : (Ljava/io/OutputStream;)V
    //   721: aload #7
    //   723: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   726: return
    //   727: aload #7
    //   729: astore_2
    //   730: aload #7
    //   732: astore #6
    //   734: new java/io/IOException
    //   737: astore_1
    //   738: aload #7
    //   740: astore_2
    //   741: aload #7
    //   743: astore #6
    //   745: aload_1
    //   746: ldc_w 'WebP files with only VP8 or VP8L chunks are currently not supported'
    //   749: invokespecial <init> : (Ljava/lang/String;)V
    //   752: aload #7
    //   754: astore_2
    //   755: aload #7
    //   757: astore #6
    //   759: aload_1
    //   760: athrow
    //   761: aload #7
    //   763: astore_2
    //   764: aload #7
    //   766: astore #6
    //   768: new java/io/IOException
    //   771: astore_1
    //   772: aload #7
    //   774: astore_2
    //   775: aload #7
    //   777: astore #6
    //   779: aload_1
    //   780: ldc_w 'Encountered invalid length while parsing WebP chunk type'
    //   783: invokespecial <init> : (Ljava/lang/String;)V
    //   786: aload #7
    //   788: astore_2
    //   789: aload #7
    //   791: astore #6
    //   793: aload_1
    //   794: athrow
    //   795: astore_1
    //   796: goto -> 825
    //   799: astore #7
    //   801: aload #6
    //   803: astore_2
    //   804: new java/io/IOException
    //   807: astore_1
    //   808: aload #6
    //   810: astore_2
    //   811: aload_1
    //   812: ldc_w 'Failed to save WebP file'
    //   815: aload #7
    //   817: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   820: aload #6
    //   822: astore_2
    //   823: aload_1
    //   824: athrow
    //   825: aload_2
    //   826: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   829: aload_1
    //   830: athrow
    // Exception table:
    //   from	to	target	type
    //   119	124	799	java/lang/Exception
    //   119	124	795	finally
    //   131	136	799	java/lang/Exception
    //   131	136	795	finally
    //   147	152	799	java/lang/Exception
    //   147	152	795	finally
    //   159	169	799	java/lang/Exception
    //   159	169	795	finally
    //   176	181	799	java/lang/Exception
    //   176	181	795	finally
    //   192	214	799	java/lang/Exception
    //   192	214	795	finally
    //   221	228	799	java/lang/Exception
    //   221	228	795	finally
    //   235	246	799	java/lang/Exception
    //   235	246	795	finally
    //   253	260	799	java/lang/Exception
    //   253	260	795	finally
    //   270	275	799	java/lang/Exception
    //   270	275	795	finally
    //   282	295	799	java/lang/Exception
    //   282	295	795	finally
    //   302	307	799	java/lang/Exception
    //   302	307	795	finally
    //   314	324	799	java/lang/Exception
    //   314	324	795	finally
    //   331	338	799	java/lang/Exception
    //   331	338	795	finally
    //   367	372	799	java/lang/Exception
    //   367	372	795	finally
    //   379	387	799	java/lang/Exception
    //   379	387	795	finally
    //   426	433	799	java/lang/Exception
    //   426	433	795	finally
    //   440	447	799	java/lang/Exception
    //   440	447	795	finally
    //   454	461	799	java/lang/Exception
    //   454	461	795	finally
    //   472	484	799	java/lang/Exception
    //   472	484	795	finally
    //   491	496	799	java/lang/Exception
    //   491	496	795	finally
    //   503	510	799	java/lang/Exception
    //   503	510	795	finally
    //   517	528	799	java/lang/Exception
    //   517	528	795	finally
    //   535	542	799	java/lang/Exception
    //   535	542	795	finally
    //   552	562	799	java/lang/Exception
    //   552	562	795	finally
    //   572	586	799	java/lang/Exception
    //   572	586	795	finally
    //   593	600	799	java/lang/Exception
    //   593	600	795	finally
    //   610	621	799	java/lang/Exception
    //   610	621	795	finally
    //   628	639	799	java/lang/Exception
    //   628	639	795	finally
    //   646	654	799	java/lang/Exception
    //   646	654	795	finally
    //   661	667	799	java/lang/Exception
    //   661	667	795	finally
    //   674	678	799	java/lang/Exception
    //   674	678	795	finally
    //   685	694	799	java/lang/Exception
    //   685	694	795	finally
    //   701	707	799	java/lang/Exception
    //   701	707	795	finally
    //   714	721	799	java/lang/Exception
    //   714	721	795	finally
    //   734	738	799	java/lang/Exception
    //   734	738	795	finally
    //   745	752	799	java/lang/Exception
    //   745	752	795	finally
    //   759	761	799	java/lang/Exception
    //   759	761	795	finally
    //   768	772	799	java/lang/Exception
    //   768	772	795	finally
    //   779	786	799	java/lang/Exception
    //   779	786	795	finally
    //   793	795	799	java/lang/Exception
    //   793	795	795	finally
    //   804	808	795	finally
    //   811	820	795	finally
    //   823	825	795	finally
  }
  
  private void setThumbnailData(ByteOrderedDataInputStream paramByteOrderedDataInputStream) throws IOException {
    HashMap<String, ExifAttribute> hashMap = this.mAttributes[4];
    ExifAttribute exifAttribute = hashMap.get("Compression");
    if (exifAttribute != null) {
      int i = exifAttribute.getIntValue(this.mExifByteOrder);
      this.mThumbnailCompression = i;
      switch (i) {
        default:
          return;
        case 6:
          handleThumbnailFromJfif(paramByteOrderedDataInputStream, hashMap);
        case 1:
        case 7:
          break;
      } 
      if (isSupportedDataType(hashMap))
        handleThumbnailFromStrips(paramByteOrderedDataInputStream, hashMap); 
    } 
    this.mThumbnailCompression = 6;
    handleThumbnailFromJfif(paramByteOrderedDataInputStream, hashMap);
  }
  
  private static boolean startsWith(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    if (paramArrayOfbyte1 == null || paramArrayOfbyte2 == null)
      return false; 
    if (paramArrayOfbyte1.length < paramArrayOfbyte2.length)
      return false; 
    for (byte b = 0; b < paramArrayOfbyte2.length; b++) {
      if (paramArrayOfbyte1[b] != paramArrayOfbyte2[b])
        return false; 
    } 
    return true;
  }
  
  private void swapBasedOnImageSize(int paramInt1, int paramInt2) throws IOException {
    if (this.mAttributes[paramInt1].isEmpty() || this.mAttributes[paramInt2].isEmpty()) {
      if (DEBUG)
        Log.d("ExifInterface", "Cannot perform swap since only one image data exists"); 
      return;
    } 
    ExifAttribute exifAttribute2 = this.mAttributes[paramInt1].get("ImageLength");
    ExifAttribute exifAttribute3 = this.mAttributes[paramInt1].get("ImageWidth");
    ExifAttribute exifAttribute4 = this.mAttributes[paramInt2].get("ImageLength");
    ExifAttribute exifAttribute1 = this.mAttributes[paramInt2].get("ImageWidth");
    if (exifAttribute2 == null || exifAttribute3 == null) {
      if (DEBUG)
        Log.d("ExifInterface", "First image does not contain valid size information"); 
      return;
    } 
    if (exifAttribute4 == null || exifAttribute1 == null) {
      if (DEBUG)
        Log.d("ExifInterface", "Second image does not contain valid size information"); 
      return;
    } 
    int i = exifAttribute2.getIntValue(this.mExifByteOrder);
    int m = exifAttribute3.getIntValue(this.mExifByteOrder);
    int k = exifAttribute4.getIntValue(this.mExifByteOrder);
    int j = exifAttribute1.getIntValue(this.mExifByteOrder);
    if (i < k && m < j) {
      HashMap<String, ExifAttribute>[] arrayOfHashMap = this.mAttributes;
      HashMap<String, ExifAttribute> hashMap = arrayOfHashMap[paramInt1];
      arrayOfHashMap[paramInt1] = arrayOfHashMap[paramInt2];
      arrayOfHashMap[paramInt2] = hashMap;
    } 
  }
  
  private void updateImageSizeValues(ByteOrderedDataInputStream paramByteOrderedDataInputStream, int paramInt) throws IOException {
    ExifAttribute exifAttribute1 = this.mAttributes[paramInt].get("DefaultCropSize");
    ExifAttribute exifAttribute5 = this.mAttributes[paramInt].get("SensorTopBorder");
    ExifAttribute exifAttribute2 = this.mAttributes[paramInt].get("SensorLeftBorder");
    ExifAttribute exifAttribute3 = this.mAttributes[paramInt].get("SensorBottomBorder");
    ExifAttribute exifAttribute4 = this.mAttributes[paramInt].get("SensorRightBorder");
    if (exifAttribute1 != null) {
      ExifAttribute exifAttribute;
      if (exifAttribute1.format == 5) {
        Rational[] arrayOfRational = (Rational[])exifAttribute1.getValue(this.mExifByteOrder);
        if (arrayOfRational == null || arrayOfRational.length != 2) {
          Log.w("ExifInterface", "Invalid crop size values. cropSize=" + Arrays.toString((Object[])arrayOfRational));
          return;
        } 
        exifAttribute1 = ExifAttribute.createURational(arrayOfRational[0], this.mExifByteOrder);
        exifAttribute = ExifAttribute.createURational(arrayOfRational[1], this.mExifByteOrder);
      } else {
        int[] arrayOfInt = (int[])exifAttribute1.getValue(this.mExifByteOrder);
        if (arrayOfInt == null || arrayOfInt.length != 2) {
          Log.w("ExifInterface", "Invalid crop size values. cropSize=" + Arrays.toString(arrayOfInt));
          return;
        } 
        exifAttribute1 = ExifAttribute.createUShort(arrayOfInt[0], this.mExifByteOrder);
        exifAttribute = ExifAttribute.createUShort(arrayOfInt[1], this.mExifByteOrder);
      } 
      this.mAttributes[paramInt].put("ImageWidth", exifAttribute1);
      this.mAttributes[paramInt].put("ImageLength", exifAttribute);
    } else {
      ExifAttribute exifAttribute;
      if (exifAttribute5 != null && exifAttribute2 != null && exifAttribute3 != null && exifAttribute4 != null) {
        int m = exifAttribute5.getIntValue(this.mExifByteOrder);
        int j = exifAttribute3.getIntValue(this.mExifByteOrder);
        int i = exifAttribute4.getIntValue(this.mExifByteOrder);
        int k = exifAttribute2.getIntValue(this.mExifByteOrder);
        if (j > m && i > k) {
          exifAttribute1 = ExifAttribute.createUShort(j - m, this.mExifByteOrder);
          exifAttribute = ExifAttribute.createUShort(i - k, this.mExifByteOrder);
          this.mAttributes[paramInt].put("ImageLength", exifAttribute1);
          this.mAttributes[paramInt].put("ImageWidth", exifAttribute);
        } 
      } else {
        retrieveJpegImageSize((ByteOrderedDataInputStream)exifAttribute, paramInt);
      } 
    } 
  }
  
  private void validateImages() throws IOException {
    swapBasedOnImageSize(0, 5);
    swapBasedOnImageSize(0, 4);
    swapBasedOnImageSize(5, 4);
    ExifAttribute exifAttribute1 = this.mAttributes[1].get("PixelXDimension");
    ExifAttribute exifAttribute2 = this.mAttributes[1].get("PixelYDimension");
    if (exifAttribute1 != null && exifAttribute2 != null) {
      this.mAttributes[0].put("ImageWidth", exifAttribute1);
      this.mAttributes[0].put("ImageLength", exifAttribute2);
    } 
    if (this.mAttributes[4].isEmpty() && isThumbnail(this.mAttributes[5])) {
      HashMap<String, ExifAttribute>[] arrayOfHashMap = this.mAttributes;
      arrayOfHashMap[4] = arrayOfHashMap[5];
      arrayOfHashMap[5] = new HashMap<>();
    } 
    if (!isThumbnail(this.mAttributes[4]))
      Log.d("ExifInterface", "No image meets the size requirements of a thumbnail image."); 
  }
  
  private int writeExifSegment(ByteOrderedDataOutputStream paramByteOrderedDataOutputStream) throws IOException {
    char c;
    ExifTag[][] arrayOfExifTag = EXIF_TAGS;
    int[] arrayOfInt2 = new int[arrayOfExifTag.length];
    int[] arrayOfInt1 = new int[arrayOfExifTag.length];
    ExifTag[] arrayOfExifTag1 = EXIF_POINTER_TAGS;
    int j = arrayOfExifTag1.length;
    int i;
    for (i = 0; i < j; i++)
      removeAttribute((arrayOfExifTag1[i]).name); 
    removeAttribute(JPEG_INTERCHANGE_FORMAT_TAG.name);
    removeAttribute(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name);
    for (i = 0; i < EXIF_TAGS.length; i++) {
      Object[] arrayOfObject = this.mAttributes[i].entrySet().toArray();
      int k = arrayOfObject.length;
      for (j = 0; j < k; j++) {
        Map.Entry entry = (Map.Entry)arrayOfObject[j];
        if (entry.getValue() == null)
          this.mAttributes[i].remove(entry.getKey()); 
      } 
    } 
    if (!this.mAttributes[1].isEmpty())
      this.mAttributes[0].put((EXIF_POINTER_TAGS[1]).name, ExifAttribute.createULong(0L, this.mExifByteOrder)); 
    if (!this.mAttributes[2].isEmpty())
      this.mAttributes[0].put((EXIF_POINTER_TAGS[2]).name, ExifAttribute.createULong(0L, this.mExifByteOrder)); 
    if (!this.mAttributes[3].isEmpty())
      this.mAttributes[1].put((EXIF_POINTER_TAGS[3]).name, ExifAttribute.createULong(0L, this.mExifByteOrder)); 
    if (this.mHasThumbnail) {
      this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong(0L, this.mExifByteOrder));
      this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name, ExifAttribute.createULong(this.mThumbnailLength, this.mExifByteOrder));
    } 
    for (i = 0; i < EXIF_TAGS.length; i++) {
      j = 0;
      Iterator<Map.Entry> iterator = this.mAttributes[i].entrySet().iterator();
      while (iterator.hasNext()) {
        int m = ((ExifAttribute)((Map.Entry)iterator.next()).getValue()).size();
        int k = j;
        if (m > 4)
          k = j + m; 
        j = k;
      } 
      arrayOfInt1[i] = arrayOfInt1[i] + j;
    } 
    i = 8;
    j = 0;
    while (j < EXIF_TAGS.length) {
      int k = i;
      if (!this.mAttributes[j].isEmpty()) {
        arrayOfInt2[j] = i;
        k = i + this.mAttributes[j].size() * 12 + 2 + 4 + arrayOfInt1[j];
      } 
      j++;
      i = k;
    } 
    j = i;
    if (this.mHasThumbnail) {
      this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong(i, this.mExifByteOrder));
      this.mThumbnailOffset = this.mExifOffset + i;
      j = i + this.mThumbnailLength;
    } 
    i = j;
    if (this.mMimeType == 4)
      i = j + 8; 
    if (DEBUG)
      for (j = 0; j < EXIF_TAGS.length; j++) {
        Log.d("ExifInterface", String.format("index: %d, offsets: %d, tag count: %d, data sizes: %d, total size: %d", new Object[] { Integer.valueOf(j), Integer.valueOf(arrayOfInt2[j]), Integer.valueOf(this.mAttributes[j].size()), Integer.valueOf(arrayOfInt1[j]), Integer.valueOf(i) }));
      }  
    if (!this.mAttributes[1].isEmpty())
      this.mAttributes[0].put((EXIF_POINTER_TAGS[1]).name, ExifAttribute.createULong(arrayOfInt2[1], this.mExifByteOrder)); 
    if (!this.mAttributes[2].isEmpty())
      this.mAttributes[0].put((EXIF_POINTER_TAGS[2]).name, ExifAttribute.createULong(arrayOfInt2[2], this.mExifByteOrder)); 
    if (!this.mAttributes[3].isEmpty())
      this.mAttributes[1].put((EXIF_POINTER_TAGS[3]).name, ExifAttribute.createULong(arrayOfInt2[3], this.mExifByteOrder)); 
    switch (this.mMimeType) {
      case 14:
        paramByteOrderedDataOutputStream.write(WEBP_CHUNK_TYPE_EXIF);
        paramByteOrderedDataOutputStream.writeInt(i);
        break;
      case 13:
        paramByteOrderedDataOutputStream.writeInt(i);
        paramByteOrderedDataOutputStream.write(PNG_CHUNK_TYPE_EXIF);
        break;
      case 4:
        paramByteOrderedDataOutputStream.writeUnsignedShort(i);
        paramByteOrderedDataOutputStream.write(IDENTIFIER_EXIF_APP1);
        break;
    } 
    if (this.mExifByteOrder == ByteOrder.BIG_ENDIAN) {
      c = '䵍';
    } else {
      c = '䥉';
    } 
    paramByteOrderedDataOutputStream.writeShort(c);
    paramByteOrderedDataOutputStream.setByteOrder(this.mExifByteOrder);
    paramByteOrderedDataOutputStream.writeUnsignedShort(42);
    paramByteOrderedDataOutputStream.writeUnsignedInt(8L);
    for (j = 0; j < EXIF_TAGS.length; j++) {
      if (!this.mAttributes[j].isEmpty()) {
        paramByteOrderedDataOutputStream.writeUnsignedShort(this.mAttributes[j].size());
        int k = arrayOfInt2[j] + 2 + this.mAttributes[j].size() * 12 + 4;
        for (Map.Entry<String, ExifAttribute> entry : this.mAttributes[j].entrySet()) {
          int m = ((ExifTag)sExifTagMapsForWriting[j].get(entry.getKey())).number;
          ExifAttribute exifAttribute = (ExifAttribute)entry.getValue();
          int n = exifAttribute.size();
          paramByteOrderedDataOutputStream.writeUnsignedShort(m);
          paramByteOrderedDataOutputStream.writeUnsignedShort(exifAttribute.format);
          paramByteOrderedDataOutputStream.writeInt(exifAttribute.numberOfComponents);
          if (n > 4) {
            paramByteOrderedDataOutputStream.writeUnsignedInt(k);
            m = k + n;
          } else {
            paramByteOrderedDataOutputStream.write(exifAttribute.bytes);
            m = k;
            if (n < 4)
              while (true) {
                m = k;
                if (n < 4) {
                  paramByteOrderedDataOutputStream.writeByte(0);
                  n++;
                  continue;
                } 
                break;
              }  
          } 
          k = m;
        } 
        if (j == 0 && !this.mAttributes[4].isEmpty()) {
          paramByteOrderedDataOutputStream.writeUnsignedInt(arrayOfInt2[4]);
        } else {
          paramByteOrderedDataOutputStream.writeUnsignedInt(0L);
        } 
        Iterator<Map.Entry> iterator = this.mAttributes[j].entrySet().iterator();
        while (iterator.hasNext()) {
          ExifAttribute exifAttribute = (ExifAttribute)((Map.Entry)iterator.next()).getValue();
          if (exifAttribute.bytes.length > 4)
            paramByteOrderedDataOutputStream.write(exifAttribute.bytes, 0, exifAttribute.bytes.length); 
        } 
      } 
    } 
    if (this.mHasThumbnail)
      paramByteOrderedDataOutputStream.write(getThumbnailBytes()); 
    if (this.mMimeType == 14 && i % 2 == 1)
      paramByteOrderedDataOutputStream.writeByte(0); 
    paramByteOrderedDataOutputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
    return i;
  }
  
  public void flipHorizontally() {
    byte b;
    switch (getAttributeInt("Orientation", 1)) {
      default:
        b = 0;
        break;
      case 8:
        b = 7;
        break;
      case 7:
        b = 8;
        break;
      case 6:
        b = 5;
        break;
      case 5:
        b = 6;
        break;
      case 4:
        b = 3;
        break;
      case 3:
        b = 4;
        break;
      case 2:
        b = 1;
        break;
      case 1:
        b = 2;
        break;
    } 
    setAttribute("Orientation", Integer.toString(b));
  }
  
  public void flipVertically() {
    byte b;
    switch (getAttributeInt("Orientation", 1)) {
      default:
        b = 0;
        break;
      case 8:
        b = 5;
        break;
      case 7:
        b = 6;
        break;
      case 6:
        b = 7;
        break;
      case 5:
        b = 8;
        break;
      case 4:
        b = 1;
        break;
      case 3:
        b = 2;
        break;
      case 2:
        b = 3;
        break;
      case 1:
        b = 4;
        break;
    } 
    setAttribute("Orientation", Integer.toString(b));
  }
  
  public double getAltitude(double paramDouble) {
    double d = getAttributeDouble("GPSAltitude", -1.0D);
    byte b = -1;
    int i = getAttributeInt("GPSAltitudeRef", -1);
    if (d >= 0.0D && i >= 0) {
      if (i != 1)
        b = 1; 
      return b * d;
    } 
    return paramDouble;
  }
  
  public String getAttribute(String paramString) {
    if (paramString != null) {
      ExifAttribute exifAttribute = getExifAttribute(paramString);
      if (exifAttribute != null) {
        if (!sTagSetForCompatibility.contains(paramString))
          return exifAttribute.getStringValue(this.mExifByteOrder); 
        if (paramString.equals("GPSTimeStamp")) {
          if (exifAttribute.format != 5 && exifAttribute.format != 10) {
            Log.w("ExifInterface", "GPS Timestamp format is not rational. format=" + exifAttribute.format);
            return null;
          } 
          Rational[] arrayOfRational = (Rational[])exifAttribute.getValue(this.mExifByteOrder);
          if (arrayOfRational == null || arrayOfRational.length != 3) {
            Log.w("ExifInterface", "Invalid GPS Timestamp array. array=" + Arrays.toString((Object[])arrayOfRational));
            return null;
          } 
          return String.format("%02d:%02d:%02d", new Object[] { Integer.valueOf((int)((float)(arrayOfRational[0]).numerator / (float)(arrayOfRational[0]).denominator)), Integer.valueOf((int)((float)(arrayOfRational[1]).numerator / (float)(arrayOfRational[1]).denominator)), Integer.valueOf((int)((float)(arrayOfRational[2]).numerator / (float)(arrayOfRational[2]).denominator)) });
        } 
        try {
          return Double.toString(exifAttribute.getDoubleValue(this.mExifByteOrder));
        } catch (NumberFormatException numberFormatException) {
          return null;
        } 
      } 
      return null;
    } 
    throw new NullPointerException("tag shouldn't be null");
  }
  
  public byte[] getAttributeBytes(String paramString) {
    if (paramString != null) {
      ExifAttribute exifAttribute = getExifAttribute(paramString);
      return (exifAttribute != null) ? exifAttribute.bytes : null;
    } 
    throw new NullPointerException("tag shouldn't be null");
  }
  
  public double getAttributeDouble(String paramString, double paramDouble) {
    if (paramString != null) {
      ExifAttribute exifAttribute = getExifAttribute(paramString);
      if (exifAttribute == null)
        return paramDouble; 
      try {
        return exifAttribute.getDoubleValue(this.mExifByteOrder);
      } catch (NumberFormatException numberFormatException) {
        return paramDouble;
      } 
    } 
    throw new NullPointerException("tag shouldn't be null");
  }
  
  public int getAttributeInt(String paramString, int paramInt) {
    if (paramString != null) {
      ExifAttribute exifAttribute = getExifAttribute(paramString);
      if (exifAttribute == null)
        return paramInt; 
      try {
        return exifAttribute.getIntValue(this.mExifByteOrder);
      } catch (NumberFormatException numberFormatException) {
        return paramInt;
      } 
    } 
    throw new NullPointerException("tag shouldn't be null");
  }
  
  public long[] getAttributeRange(String paramString) {
    if (paramString != null) {
      if (!this.mModified) {
        ExifAttribute exifAttribute = getExifAttribute(paramString);
        return (exifAttribute != null) ? new long[] { exifAttribute.bytesOffset, exifAttribute.bytes.length } : null;
      } 
      throw new IllegalStateException("The underlying file has been modified since being parsed");
    } 
    throw new NullPointerException("tag shouldn't be null");
  }
  
  public long getDateTime() {
    return parseDateTime(getAttribute("DateTime"), getAttribute("SubSecTime"));
  }
  
  public long getDateTimeDigitized() {
    return parseDateTime(getAttribute("DateTimeDigitized"), getAttribute("SubSecTimeDigitized"));
  }
  
  public long getDateTimeOriginal() {
    return parseDateTime(getAttribute("DateTimeOriginal"), getAttribute("SubSecTimeOriginal"));
  }
  
  public long getGpsDateTime() {
    String str1 = getAttribute("GPSDateStamp");
    String str2 = getAttribute("GPSTimeStamp");
    if (str1 != null && str2 != null) {
      Pattern pattern = sNonZeroTimePattern;
      if (pattern.matcher(str1).matches() || pattern.matcher(str2).matches()) {
        str1 = str1 + ' ' + str2;
        ParsePosition parsePosition = new ParsePosition(0);
        try {
          Date date = sFormatter.parse(str1, parsePosition);
          return (date == null) ? -1L : date.getTime();
        } catch (IllegalArgumentException illegalArgumentException) {
          return -1L;
        } 
      } 
    } 
    return -1L;
  }
  
  @Deprecated
  public boolean getLatLong(float[] paramArrayOffloat) {
    double[] arrayOfDouble = getLatLong();
    if (arrayOfDouble == null)
      return false; 
    paramArrayOffloat[0] = (float)arrayOfDouble[0];
    paramArrayOffloat[1] = (float)arrayOfDouble[1];
    return true;
  }
  
  public double[] getLatLong() {
    String str3 = getAttribute("GPSLatitude");
    String str4 = getAttribute("GPSLatitudeRef");
    String str1 = getAttribute("GPSLongitude");
    String str2 = getAttribute("GPSLongitudeRef");
    if (str3 != null && str4 != null && str1 != null && str2 != null)
      try {
        double d1 = convertRationalLatLonToDouble(str3, str4);
        double d2 = convertRationalLatLonToDouble(str1, str2);
        return new double[] { d1, d2 };
      } catch (IllegalArgumentException illegalArgumentException) {
        Log.w("ExifInterface", "Latitude/longitude values are not parsable. " + String.format("latValue=%s, latRef=%s, lngValue=%s, lngRef=%s", new Object[] { str3, str4, str1, str2 }));
      }  
    return null;
  }
  
  public int getRotationDegrees() {
    switch (getAttributeInt("Orientation", 1)) {
      default:
        return 0;
      case 6:
      case 7:
        return 90;
      case 5:
      case 8:
        return 270;
      case 3:
      case 4:
        break;
    } 
    return 180;
  }
  
  public byte[] getThumbnail() {
    int i = this.mThumbnailCompression;
    return (i == 6 || i == 7) ? getThumbnailBytes() : null;
  }
  
  public Bitmap getThumbnailBitmap() {
    if (!this.mHasThumbnail)
      return null; 
    if (this.mThumbnailBytes == null)
      this.mThumbnailBytes = getThumbnailBytes(); 
    int i = this.mThumbnailCompression;
    if (i == 6 || i == 7)
      return BitmapFactory.decodeByteArray(this.mThumbnailBytes, 0, this.mThumbnailLength); 
    if (i == 1) {
      int[] arrayOfInt = new int[this.mThumbnailBytes.length / 3];
      for (i = 0; i < arrayOfInt.length; i++) {
        byte[] arrayOfByte = this.mThumbnailBytes;
        arrayOfInt[i] = (arrayOfByte[i * 3] << 16) + 0 + (arrayOfByte[i * 3 + 1] << 8) + arrayOfByte[i * 3 + 2];
      } 
      ExifAttribute exifAttribute2 = this.mAttributes[4].get("ImageLength");
      ExifAttribute exifAttribute1 = this.mAttributes[4].get("ImageWidth");
      if (exifAttribute2 != null && exifAttribute1 != null) {
        i = exifAttribute2.getIntValue(this.mExifByteOrder);
        return Bitmap.createBitmap(arrayOfInt, exifAttribute1.getIntValue(this.mExifByteOrder), i, Bitmap.Config.ARGB_8888);
      } 
    } 
    return null;
  }
  
  public byte[] getThumbnailBytes() {
    FileInputStream fileInputStream1;
    FileInputStream fileInputStream2;
    if (!this.mHasThumbnail)
      return null; 
    byte[] arrayOfByte = this.mThumbnailBytes;
    if (arrayOfByte != null)
      return arrayOfByte; 
    AssetManager.AssetInputStream assetInputStream4 = null;
    AssetManager.AssetInputStream assetInputStream3 = null;
    AssetManager.AssetInputStream assetInputStream5 = null;
    FileDescriptor fileDescriptor5 = null;
    FileDescriptor fileDescriptor4 = null;
    FileDescriptor fileDescriptor3 = null;
    AssetManager.AssetInputStream assetInputStream1 = assetInputStream4;
    FileDescriptor fileDescriptor1 = fileDescriptor5;
    AssetManager.AssetInputStream assetInputStream2 = assetInputStream3;
    FileDescriptor fileDescriptor2 = fileDescriptor4;
    try {
      FileInputStream fileInputStream;
      FileDescriptor fileDescriptor;
      AssetManager.AssetInputStream assetInputStream = this.mAssetInputStream;
      if (assetInputStream != null) {
        assetInputStream1 = assetInputStream;
        fileDescriptor1 = fileDescriptor5;
        assetInputStream2 = assetInputStream;
        fileDescriptor2 = fileDescriptor4;
        if (assetInputStream.markSupported()) {
          assetInputStream1 = assetInputStream;
          fileDescriptor1 = fileDescriptor5;
          assetInputStream2 = assetInputStream;
          fileDescriptor2 = fileDescriptor4;
          assetInputStream.reset();
          fileDescriptor = fileDescriptor3;
        } else {
          assetInputStream1 = assetInputStream;
          fileDescriptor1 = fileDescriptor5;
          assetInputStream2 = assetInputStream;
          fileDescriptor2 = fileDescriptor4;
          Log.d("ExifInterface", "Cannot read thumbnail from inputstream without mark/reset support");
          closeQuietly((Closeable)assetInputStream);
          if (false)
            closeFileDescriptor(null); 
          return null;
        } 
      } else {
        assetInputStream1 = assetInputStream4;
        fileDescriptor1 = fileDescriptor5;
        assetInputStream2 = assetInputStream3;
        fileDescriptor2 = fileDescriptor4;
        if (this.mFilename != null) {
          assetInputStream1 = assetInputStream4;
          fileDescriptor1 = fileDescriptor5;
          assetInputStream2 = assetInputStream3;
          fileDescriptor2 = fileDescriptor4;
          fileInputStream = new FileInputStream(this.mFilename);
          fileDescriptor = fileDescriptor3;
        } else {
          assetInputStream = assetInputStream5;
          fileDescriptor = fileDescriptor3;
          assetInputStream1 = assetInputStream4;
          fileDescriptor1 = fileDescriptor5;
          assetInputStream2 = assetInputStream3;
          fileDescriptor2 = fileDescriptor4;
          if (Build.VERSION.SDK_INT >= 21) {
            assetInputStream1 = assetInputStream4;
            fileDescriptor1 = fileDescriptor5;
            assetInputStream2 = assetInputStream3;
            fileDescriptor2 = fileDescriptor4;
            FileDescriptor fileDescriptor6 = this.mSeekableFileDescriptor;
            assetInputStream = assetInputStream5;
            fileDescriptor = fileDescriptor3;
            if (fileDescriptor6 != null) {
              assetInputStream1 = assetInputStream4;
              fileDescriptor1 = fileDescriptor5;
              assetInputStream2 = assetInputStream3;
              fileDescriptor2 = fileDescriptor4;
              fileDescriptor = Os.dup(fileDescriptor6);
              assetInputStream1 = assetInputStream4;
              fileDescriptor1 = fileDescriptor;
              assetInputStream2 = assetInputStream3;
              fileDescriptor2 = fileDescriptor;
              Os.lseek(fileDescriptor, 0L, OsConstants.SEEK_SET);
              assetInputStream1 = assetInputStream4;
              fileDescriptor1 = fileDescriptor;
              assetInputStream2 = assetInputStream3;
              fileDescriptor2 = fileDescriptor;
              fileInputStream = new FileInputStream(fileDescriptor);
            } 
          } 
        } 
      } 
      if (fileInputStream != null) {
        FileInputStream fileInputStream3 = fileInputStream;
        fileDescriptor1 = fileDescriptor;
        FileInputStream fileInputStream4 = fileInputStream;
        fileDescriptor2 = fileDescriptor;
        long l = fileInputStream.skip(this.mThumbnailOffset);
        fileInputStream3 = fileInputStream;
        fileDescriptor1 = fileDescriptor;
        fileInputStream4 = fileInputStream;
        fileDescriptor2 = fileDescriptor;
        int i = this.mThumbnailOffset;
        if (l == i) {
          fileInputStream3 = fileInputStream;
          fileDescriptor1 = fileDescriptor;
          fileInputStream4 = fileInputStream;
          fileDescriptor2 = fileDescriptor;
          byte[] arrayOfByte1 = new byte[this.mThumbnailLength];
          fileInputStream3 = fileInputStream;
          fileDescriptor1 = fileDescriptor;
          fileInputStream4 = fileInputStream;
          fileDescriptor2 = fileDescriptor;
          if (fileInputStream.read(arrayOfByte1) == this.mThumbnailLength) {
            fileInputStream3 = fileInputStream;
            fileDescriptor1 = fileDescriptor;
            fileInputStream4 = fileInputStream;
            fileDescriptor2 = fileDescriptor;
            this.mThumbnailBytes = arrayOfByte1;
            closeQuietly(fileInputStream);
            if (fileDescriptor != null)
              closeFileDescriptor(fileDescriptor); 
            return arrayOfByte1;
          } 
          fileInputStream3 = fileInputStream;
          fileDescriptor1 = fileDescriptor;
          fileInputStream4 = fileInputStream;
          fileDescriptor2 = fileDescriptor;
          IOException iOException1 = new IOException();
          fileInputStream3 = fileInputStream;
          fileDescriptor1 = fileDescriptor;
          fileInputStream4 = fileInputStream;
          fileDescriptor2 = fileDescriptor;
          this("Corrupted image");
          fileInputStream3 = fileInputStream;
          fileDescriptor1 = fileDescriptor;
          fileInputStream4 = fileInputStream;
          fileDescriptor2 = fileDescriptor;
          throw iOException1;
        } 
        fileInputStream3 = fileInputStream;
        fileDescriptor1 = fileDescriptor;
        fileInputStream4 = fileInputStream;
        fileDescriptor2 = fileDescriptor;
        IOException iOException = new IOException();
        fileInputStream3 = fileInputStream;
        fileDescriptor1 = fileDescriptor;
        fileInputStream4 = fileInputStream;
        fileDescriptor2 = fileDescriptor;
        this("Corrupted image");
        fileInputStream3 = fileInputStream;
        fileDescriptor1 = fileDescriptor;
        fileInputStream4 = fileInputStream;
        fileDescriptor2 = fileDescriptor;
        throw iOException;
      } 
      fileInputStream1 = fileInputStream;
      fileDescriptor1 = fileDescriptor;
      fileInputStream2 = fileInputStream;
      fileDescriptor2 = fileDescriptor;
      FileNotFoundException fileNotFoundException = new FileNotFoundException();
      fileInputStream1 = fileInputStream;
      fileDescriptor1 = fileDescriptor;
      fileInputStream2 = fileInputStream;
      fileDescriptor2 = fileDescriptor;
      this();
      fileInputStream1 = fileInputStream;
      fileDescriptor1 = fileDescriptor;
      fileInputStream2 = fileInputStream;
      fileDescriptor2 = fileDescriptor;
      throw fileNotFoundException;
    } catch (Exception exception) {
      fileInputStream1 = fileInputStream2;
      fileDescriptor1 = fileDescriptor2;
      Log.d("ExifInterface", "Encountered exception while getting thumbnail", exception);
      closeQuietly(fileInputStream2);
      if (fileDescriptor2 != null)
        closeFileDescriptor(fileDescriptor2); 
      return null;
    } finally {}
    closeQuietly(fileInputStream1);
    if (fileDescriptor1 != null)
      closeFileDescriptor(fileDescriptor1); 
    throw arrayOfByte;
  }
  
  public long[] getThumbnailRange() {
    if (!this.mModified)
      return this.mHasThumbnail ? ((this.mHasThumbnailStrips && !this.mAreThumbnailStripsConsecutive) ? null : new long[] { this.mThumbnailOffset, this.mThumbnailLength }) : null; 
    throw new IllegalStateException("The underlying file has been modified since being parsed");
  }
  
  public boolean hasAttribute(String paramString) {
    boolean bool;
    if (getExifAttribute(paramString) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasThumbnail() {
    return this.mHasThumbnail;
  }
  
  public boolean isFlipped() {
    switch (getAttributeInt("Orientation", 1)) {
      default:
        return false;
      case 2:
      case 4:
      case 5:
      case 7:
        break;
    } 
    return true;
  }
  
  public boolean isThumbnailCompressed() {
    if (!this.mHasThumbnail)
      return false; 
    int i = this.mThumbnailCompression;
    return (i == 6 || i == 7);
  }
  
  public void resetOrientation() {
    setAttribute("Orientation", Integer.toString(1));
  }
  
  public void rotate(int paramInt) {
    if (paramInt % 90 == 0) {
      int k = getAttributeInt("Orientation", 1);
      List<Integer> list = ROTATION_ORDER;
      boolean bool = list.contains(Integer.valueOf(k));
      int i = 0;
      int j = 0;
      if (bool) {
        i = list.indexOf(Integer.valueOf(k));
        i = (paramInt / 90 + i) % 4;
        paramInt = j;
        if (i < 0)
          paramInt = 4; 
        paramInt = ((Integer)list.get(i + paramInt)).intValue();
      } else {
        list = FLIPPED_ROTATION_ORDER;
        if (list.contains(Integer.valueOf(k))) {
          j = list.indexOf(Integer.valueOf(k));
          j = (paramInt / 90 + j) % 4;
          paramInt = i;
          if (j < 0)
            paramInt = 4; 
          paramInt = ((Integer)list.get(j + paramInt)).intValue();
        } else {
          paramInt = 0;
        } 
      } 
      setAttribute("Orientation", Integer.toString(paramInt));
      return;
    } 
    throw new IllegalArgumentException("degree should be a multiple of 90");
  }
  
  public void saveAttributes() throws IOException {
    if (isSupportedFormatForSavingAttributes()) {
      if (this.mSeekableFileDescriptor != null || this.mFilename != null) {
        Exception exception;
        IOException iOException1;
        StringBuilder stringBuilder1;
        StringBuilder stringBuilder2;
        IOException iOException2;
        this.mModified = true;
        this.mThumbnailBytes = getThumbnail();
        FileInputStream fileInputStream4 = null;
        FileInputStream fileInputStream3 = null;
        StringBuilder stringBuilder3 = null;
        FileOutputStream fileOutputStream3 = null;
        FileOutputStream fileOutputStream4 = null;
        FileOutputStream fileOutputStream5 = null;
        File file1 = null;
        if (this.mFilename != null)
          file1 = new File(this.mFilename); 
        File file2 = null;
        FileInputStream fileInputStream1 = fileInputStream4;
        FileOutputStream fileOutputStream1 = fileOutputStream3;
        FileInputStream fileInputStream2 = fileInputStream3;
        FileOutputStream fileOutputStream2 = fileOutputStream4;
        try {
          FileInputStream fileInputStream;
          BufferedInputStream bufferedInputStream1;
          BufferedOutputStream bufferedOutputStream1;
          File file;
          BufferedOutputStream bufferedOutputStream2;
          BufferedInputStream bufferedInputStream2;
          if (this.mFilename != null) {
            fileInputStream1 = fileInputStream4;
            fileOutputStream1 = fileOutputStream3;
            fileInputStream2 = fileInputStream3;
            fileOutputStream2 = fileOutputStream4;
            file = new File();
            fileInputStream1 = fileInputStream4;
            fileOutputStream1 = fileOutputStream3;
            fileInputStream2 = fileInputStream3;
            fileOutputStream2 = fileOutputStream4;
            StringBuilder stringBuilder = new StringBuilder();
            fileInputStream1 = fileInputStream4;
            fileOutputStream1 = fileOutputStream3;
            fileInputStream2 = fileInputStream3;
            fileOutputStream2 = fileOutputStream4;
            this();
            fileInputStream1 = fileInputStream4;
            fileOutputStream1 = fileOutputStream3;
            fileInputStream2 = fileInputStream3;
            fileOutputStream2 = fileOutputStream4;
            this(stringBuilder.append(this.mFilename).append(".tmp").toString());
            fileInputStream1 = fileInputStream4;
            fileOutputStream1 = fileOutputStream3;
            fileInputStream2 = fileInputStream3;
            fileOutputStream2 = fileOutputStream4;
            if (file1.renameTo(file)) {
              stringBuilder = stringBuilder3;
              fileOutputStream7 = fileOutputStream5;
            } else {
              fileInputStream1 = fileInputStream4;
              fileOutputStream1 = fileOutputStream3;
              fileInputStream2 = fileInputStream3;
              fileOutputStream2 = fileOutputStream4;
              IOException iOException = new IOException();
              fileInputStream1 = fileInputStream4;
              fileOutputStream1 = fileOutputStream3;
              fileInputStream2 = fileInputStream3;
              fileOutputStream2 = fileOutputStream4;
              stringBuilder = new StringBuilder();
              fileInputStream1 = fileInputStream4;
              fileOutputStream1 = fileOutputStream3;
              fileInputStream2 = fileInputStream3;
              fileOutputStream2 = fileOutputStream4;
              this();
              fileInputStream1 = fileInputStream4;
              fileOutputStream1 = fileOutputStream3;
              fileInputStream2 = fileInputStream3;
              fileOutputStream2 = fileOutputStream4;
              this(stringBuilder.append("Couldn't rename to ").append(file.getAbsolutePath()).toString());
              fileInputStream1 = fileInputStream4;
              fileOutputStream1 = fileOutputStream3;
              fileInputStream2 = fileInputStream3;
              fileOutputStream2 = fileOutputStream4;
              throw iOException;
            } 
          } else {
            StringBuilder stringBuilder = stringBuilder3;
            fileOutputStream7 = fileOutputStream5;
            file = file2;
            fileInputStream1 = fileInputStream4;
            fileOutputStream1 = fileOutputStream3;
            fileInputStream2 = fileInputStream3;
            fileOutputStream2 = fileOutputStream4;
            if (Build.VERSION.SDK_INT >= 21) {
              stringBuilder = stringBuilder3;
              fileOutputStream7 = fileOutputStream5;
              file = file2;
              fileInputStream1 = fileInputStream4;
              fileOutputStream1 = fileOutputStream3;
              fileInputStream2 = fileInputStream3;
              fileOutputStream2 = fileOutputStream4;
              if (this.mSeekableFileDescriptor != null) {
                fileInputStream1 = fileInputStream4;
                fileOutputStream1 = fileOutputStream3;
                fileInputStream2 = fileInputStream3;
                fileOutputStream2 = fileOutputStream4;
                file = File.createTempFile("temp", "tmp");
                fileInputStream1 = fileInputStream4;
                fileOutputStream1 = fileOutputStream3;
                fileInputStream2 = fileInputStream3;
                fileOutputStream2 = fileOutputStream4;
                Os.lseek(this.mSeekableFileDescriptor, 0L, OsConstants.SEEK_SET);
                fileInputStream1 = fileInputStream4;
                fileOutputStream1 = fileOutputStream3;
                fileInputStream2 = fileInputStream3;
                fileOutputStream2 = fileOutputStream4;
                fileInputStream = new FileInputStream();
                fileInputStream1 = fileInputStream4;
                fileOutputStream1 = fileOutputStream3;
                fileInputStream2 = fileInputStream3;
                fileOutputStream2 = fileOutputStream4;
                this(this.mSeekableFileDescriptor);
                fileInputStream1 = fileInputStream;
                fileOutputStream1 = fileOutputStream3;
                fileInputStream2 = fileInputStream;
                fileOutputStream2 = fileOutputStream4;
                fileOutputStream7 = new FileOutputStream();
                fileInputStream1 = fileInputStream;
                fileOutputStream1 = fileOutputStream3;
                fileInputStream2 = fileInputStream;
                fileOutputStream2 = fileOutputStream4;
                this(file);
                fileInputStream1 = fileInputStream;
                fileOutputStream1 = fileOutputStream7;
                fileInputStream2 = fileInputStream;
                fileOutputStream2 = fileOutputStream7;
                copy(fileInputStream, fileOutputStream7);
              } 
            } 
          } 
          closeQuietly(fileInputStream);
          closeQuietly(fileOutputStream7);
          fileInputStream4 = null;
          fileInputStream3 = null;
          fileOutputStream2 = null;
          fileOutputStream4 = null;
          fileOutputStream3 = null;
          FileOutputStream fileOutputStream6 = fileOutputStream2;
          FileOutputStream fileOutputStream7 = fileOutputStream3;
          fileInputStream1 = fileInputStream3;
          fileOutputStream1 = fileOutputStream4;
          try {
            FileOutputStream fileOutputStream;
            FileInputStream fileInputStream5 = new FileInputStream();
            fileOutputStream6 = fileOutputStream2;
            fileOutputStream7 = fileOutputStream3;
            fileInputStream1 = fileInputStream3;
            fileOutputStream1 = fileOutputStream4;
            this(file);
            fileOutputStream6 = fileOutputStream2;
            fileOutputStream7 = fileOutputStream3;
            fileInputStream1 = fileInputStream3;
            fileOutputStream1 = fileOutputStream4;
            if (this.mFilename != null) {
              fileOutputStream6 = fileOutputStream2;
              fileOutputStream7 = fileOutputStream3;
              fileInputStream1 = fileInputStream3;
              fileOutputStream1 = fileOutputStream4;
              fileOutputStream = new FileOutputStream();
              fileOutputStream6 = fileOutputStream2;
              fileOutputStream7 = fileOutputStream3;
              fileInputStream1 = fileInputStream3;
              fileOutputStream1 = fileOutputStream4;
              this(this.mFilename);
            } else {
              fileInputStream2 = fileInputStream4;
              fileOutputStream6 = fileOutputStream2;
              fileOutputStream7 = fileOutputStream3;
              fileInputStream1 = fileInputStream3;
              fileOutputStream1 = fileOutputStream4;
              if (Build.VERSION.SDK_INT >= 21) {
                fileOutputStream6 = fileOutputStream2;
                fileOutputStream7 = fileOutputStream3;
                fileInputStream1 = fileInputStream3;
                fileOutputStream1 = fileOutputStream4;
                FileDescriptor fileDescriptor = this.mSeekableFileDescriptor;
                fileInputStream2 = fileInputStream4;
                if (fileDescriptor != null) {
                  fileOutputStream6 = fileOutputStream2;
                  fileOutputStream7 = fileOutputStream3;
                  fileInputStream1 = fileInputStream3;
                  fileOutputStream1 = fileOutputStream4;
                  Os.lseek(fileDescriptor, 0L, OsConstants.SEEK_SET);
                  fileOutputStream6 = fileOutputStream2;
                  fileOutputStream7 = fileOutputStream3;
                  fileInputStream1 = fileInputStream3;
                  fileOutputStream1 = fileOutputStream4;
                  fileOutputStream = new FileOutputStream(this.mSeekableFileDescriptor);
                } 
              } 
            } 
            fileOutputStream6 = fileOutputStream2;
            fileOutputStream7 = fileOutputStream3;
            fileInputStream1 = fileInputStream3;
            fileOutputStream1 = fileOutputStream4;
            BufferedInputStream bufferedInputStream4 = new BufferedInputStream();
            fileOutputStream6 = fileOutputStream2;
            fileOutputStream7 = fileOutputStream3;
            fileInputStream1 = fileInputStream3;
            fileOutputStream1 = fileOutputStream4;
            this(fileInputStream5);
            BufferedInputStream bufferedInputStream3 = bufferedInputStream4;
            bufferedInputStream1 = bufferedInputStream3;
            fileOutputStream7 = fileOutputStream3;
            bufferedInputStream2 = bufferedInputStream3;
            fileOutputStream1 = fileOutputStream4;
            BufferedOutputStream bufferedOutputStream4 = new BufferedOutputStream();
            bufferedInputStream1 = bufferedInputStream3;
            fileOutputStream7 = fileOutputStream3;
            bufferedInputStream2 = bufferedInputStream3;
            fileOutputStream1 = fileOutputStream4;
            this(fileOutputStream);
            BufferedOutputStream bufferedOutputStream3 = bufferedOutputStream4;
            bufferedInputStream1 = bufferedInputStream3;
            bufferedOutputStream1 = bufferedOutputStream3;
            bufferedInputStream2 = bufferedInputStream3;
            bufferedOutputStream2 = bufferedOutputStream3;
            int i = this.mMimeType;
            if (i == 4) {
              bufferedInputStream1 = bufferedInputStream3;
              bufferedOutputStream1 = bufferedOutputStream3;
              bufferedInputStream2 = bufferedInputStream3;
              bufferedOutputStream2 = bufferedOutputStream3;
              saveJpegAttributes(bufferedInputStream3, bufferedOutputStream3);
            } else if (i == 13) {
              bufferedInputStream1 = bufferedInputStream3;
              bufferedOutputStream1 = bufferedOutputStream3;
              bufferedInputStream2 = bufferedInputStream3;
              bufferedOutputStream2 = bufferedOutputStream3;
              savePngAttributes(bufferedInputStream3, bufferedOutputStream3);
            } else if (i == 14) {
              bufferedInputStream1 = bufferedInputStream3;
              bufferedOutputStream1 = bufferedOutputStream3;
              bufferedInputStream2 = bufferedInputStream3;
              bufferedOutputStream2 = bufferedOutputStream3;
              saveWebpAttributes(bufferedInputStream3, bufferedOutputStream3);
            } 
            closeQuietly(bufferedInputStream3);
            closeQuietly(bufferedOutputStream3);
            file.delete();
            this.mThumbnailBytes = null;
            return;
          } catch (Exception exception1) {
            bufferedInputStream1 = bufferedInputStream2;
            bufferedOutputStream1 = bufferedOutputStream2;
            if (this.mFilename != null) {
              bufferedInputStream1 = bufferedInputStream2;
              bufferedOutputStream1 = bufferedOutputStream2;
              if (!file.renameTo(file1)) {
                bufferedInputStream1 = bufferedInputStream2;
                bufferedOutputStream1 = bufferedOutputStream2;
                IOException iOException = new IOException();
                bufferedInputStream1 = bufferedInputStream2;
                bufferedOutputStream1 = bufferedOutputStream2;
                stringBuilder2 = new StringBuilder();
                bufferedInputStream1 = bufferedInputStream2;
                bufferedOutputStream1 = bufferedOutputStream2;
                this();
                bufferedInputStream1 = bufferedInputStream2;
                bufferedOutputStream1 = bufferedOutputStream2;
                this(stringBuilder2.append("Couldn't restore original file: ").append(file1.getAbsolutePath()).toString());
                bufferedInputStream1 = bufferedInputStream2;
                bufferedOutputStream1 = bufferedOutputStream2;
                throw iOException;
              } 
            } 
            bufferedInputStream1 = bufferedInputStream2;
            bufferedOutputStream1 = bufferedOutputStream2;
            iOException2 = new IOException();
            bufferedInputStream1 = bufferedInputStream2;
            bufferedOutputStream1 = bufferedOutputStream2;
            this("Failed to save new file", (Throwable)stringBuilder2);
            bufferedInputStream1 = bufferedInputStream2;
            bufferedOutputStream1 = bufferedOutputStream2;
            throw iOException2;
          } finally {}
          closeQuietly(bufferedInputStream1);
          closeQuietly(bufferedOutputStream1);
          file.delete();
          throw bufferedOutputStream2;
        } catch (Exception null) {
          stringBuilder1 = stringBuilder2;
          iOException1 = iOException2;
          IOException iOException = new IOException();
          stringBuilder1 = stringBuilder2;
          iOException1 = iOException2;
          this("Failed to copy original file to temp file", exception);
          stringBuilder1 = stringBuilder2;
          iOException1 = iOException2;
          throw iOException;
        } finally {}
        closeQuietly((Closeable)stringBuilder1);
        closeQuietly((Closeable)iOException1);
        throw exception;
      } 
      throw new IOException("ExifInterface does not support saving attributes for the current input.");
    } 
    throw new IOException("ExifInterface only supports saving attributes on JPEG, PNG, or WebP formats.");
  }
  
  public void setAltitude(double paramDouble) {
    String str;
    if (paramDouble >= 0.0D) {
      str = "0";
    } else {
      str = "1";
    } 
    setAttribute("GPSAltitude", (new Rational(Math.abs(paramDouble))).toString());
    setAttribute("GPSAltitudeRef", str);
  }
  
  public void setAttribute(String paramString1, String paramString2) {
    String str = paramString2;
    if (paramString1 != null) {
      String str1;
      if ("ISOSpeedRatings".equals(paramString1)) {
        if (DEBUG)
          Log.d("ExifInterface", "setAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY."); 
        str1 = "PhotographicSensitivity";
      } else {
        str1 = paramString1;
      } 
      int j = 1;
      paramString1 = str;
      if (str != null) {
        paramString1 = str;
        if (sTagSetForCompatibility.contains(str1))
          if (str1.equals("GPSTimeStamp")) {
            Matcher matcher = sGpsTimestampPattern.matcher(str);
            if (!matcher.find()) {
              Log.w("ExifInterface", "Invalid value for " + str1 + " : " + str);
              return;
            } 
            String str2 = Integer.parseInt(matcher.group(1)) + "/1," + Integer.parseInt(matcher.group(2)) + "/1," + Integer.parseInt(matcher.group(3)) + "/1";
          } else {
            try {
              double d = Double.parseDouble(paramString2);
              Rational rational = new Rational();
              this(d);
              String str2 = rational.toString();
            } catch (NumberFormatException numberFormatException) {
              Log.w("ExifInterface", "Invalid value for " + str1 + " : " + str);
              return;
            } 
          }  
      } 
      int i;
      for (i = 0;; i = k) {
        if (i < EXIF_TAGS.length) {
          if (i == 4 && !this.mHasThumbnail) {
            int m = j;
            j = i;
            i = m;
          } else {
            ExifTag exifTag = sExifTagMapsForWriting[i].get(str1);
            if (exifTag != null) {
              if (numberFormatException == null) {
                this.mAttributes[i].remove(str1);
                int m = i;
                i = j;
                j = m;
              } else {
                String[] arrayOfString2;
                int[] arrayOfInt2;
                String[] arrayOfString1;
                long[] arrayOfLong;
                int[] arrayOfInt1;
                int m;
                int n;
                int i1;
                double[] arrayOfDouble;
                String[] arrayOfString4;
                Rational[] arrayOfRational1;
                String[] arrayOfString3;
                Rational[] arrayOfRational2;
                String[] arrayOfString5;
                Rational[] arrayOfRational3;
                Pair<Integer, Integer> pair = guessDataFormat((String)numberFormatException);
                if (exifTag.primaryFormat == ((Integer)pair.first).intValue() || exifTag.primaryFormat == ((Integer)pair.second).intValue()) {
                  m = exifTag.primaryFormat;
                } else if (exifTag.secondaryFormat != -1 && (exifTag.secondaryFormat == ((Integer)pair.first).intValue() || exifTag.secondaryFormat == ((Integer)pair.second).intValue())) {
                  m = exifTag.secondaryFormat;
                } else if (exifTag.primaryFormat == j || exifTag.primaryFormat == 7 || exifTag.primaryFormat == 2) {
                  m = exifTag.primaryFormat;
                } else {
                  if (DEBUG) {
                    String str2;
                    StringBuilder stringBuilder1 = (new StringBuilder()).append("Given tag (").append(str1).append(") value didn't match with one of expected formats: ");
                    String[] arrayOfString = IFD_FORMAT_NAMES;
                    StringBuilder stringBuilder3 = stringBuilder1.append(arrayOfString[exifTag.primaryFormat]);
                    int i2 = exifTag.secondaryFormat;
                    String str3 = "";
                    if (i2 == -1) {
                      str2 = "";
                    } else {
                      str2 = ", " + arrayOfString[exifTag.secondaryFormat];
                    } 
                    StringBuilder stringBuilder2 = stringBuilder3.append(str2).append(" (guess: ").append(arrayOfString[((Integer)pair.first).intValue()]);
                    if (((Integer)pair.second).intValue() == -1) {
                      str2 = str3;
                    } else {
                      str2 = ", " + arrayOfString[((Integer)pair.second).intValue()];
                    } 
                    Log.d("ExifInterface", stringBuilder2.append(str2).append(")").toString());
                    i2 = i;
                    i = j;
                    j = i2;
                  } else {
                    int i2 = j;
                    j = i;
                    i = i2;
                  } 
                  m = j + 1;
                  j = i;
                  i = m;
                } 
                switch (m) {
                  default:
                    n = j;
                    i1 = i;
                    i = n;
                    j = i1;
                    if (DEBUG) {
                      Log.d("ExifInterface", "Data format isn't one of expected formats: " + m);
                      i = n;
                      j = i1;
                    } 
                    break;
                  case 12:
                    arrayOfString2 = numberFormatException.split(",", -1);
                    arrayOfDouble = new double[arrayOfString2.length];
                    for (m = 0; m < arrayOfString2.length; m++)
                      arrayOfDouble[m] = Double.parseDouble(arrayOfString2[m]); 
                    this.mAttributes[i].put(str1, ExifAttribute.createDouble(arrayOfDouble, this.mExifByteOrder));
                    m = j;
                    j = i;
                    i = m;
                    break;
                  case 10:
                    arrayOfString2 = numberFormatException.split(",", -1);
                    arrayOfRational2 = new Rational[arrayOfString2.length];
                    m = 0;
                    while (m < arrayOfString2.length) {
                      String[] arrayOfString = arrayOfString2[m].split("/", -1);
                      arrayOfRational2[m] = new Rational((long)Double.parseDouble(arrayOfString[0]), (long)Double.parseDouble(arrayOfString[j]));
                      m++;
                      j = 1;
                    } 
                    j = i;
                    this.mAttributes[j].put(str1, ExifAttribute.createSRational(arrayOfRational2, this.mExifByteOrder));
                    i = 1;
                    break;
                  case 9:
                    j = i;
                    arrayOfString4 = numberFormatException.split(",", -1);
                    arrayOfInt2 = new int[arrayOfString4.length];
                    for (i = 0; i < arrayOfString4.length; i++)
                      arrayOfInt2[i] = Integer.parseInt(arrayOfString4[i]); 
                    this.mAttributes[j].put(str1, ExifAttribute.createSLong(arrayOfInt2, this.mExifByteOrder));
                    i = 1;
                    break;
                  case 5:
                    j = i;
                    arrayOfString5 = numberFormatException.split(",", -1);
                    arrayOfRational3 = new Rational[arrayOfString5.length];
                    i = 0;
                    arrayOfString1 = arrayOfString4;
                    arrayOfRational1 = arrayOfRational2;
                    while (i < arrayOfString5.length) {
                      String[] arrayOfString = arrayOfString5[i].split("/", -1);
                      arrayOfRational3[i] = new Rational((long)Double.parseDouble(arrayOfString[0]), (long)Double.parseDouble(arrayOfString[1]));
                      i++;
                    } 
                    i = 1;
                    this.mAttributes[j].put(str1, ExifAttribute.createURational(arrayOfRational3, this.mExifByteOrder));
                    break;
                  case 4:
                    m = j;
                    j = i;
                    arrayOfString3 = numberFormatException.split(",", -1);
                    arrayOfLong = new long[arrayOfString3.length];
                    for (i = 0; i < arrayOfString3.length; i++)
                      arrayOfLong[i] = Long.parseLong(arrayOfString3[i]); 
                    this.mAttributes[j].put(str1, ExifAttribute.createULong(arrayOfLong, this.mExifByteOrder));
                    i = m;
                    break;
                  case 3:
                    m = i;
                    arrayOfString3 = numberFormatException.split(",", -1);
                    arrayOfInt1 = new int[arrayOfString3.length];
                    for (i = 0; i < arrayOfString3.length; i++)
                      arrayOfInt1[i] = Integer.parseInt(arrayOfString3[i]); 
                    this.mAttributes[m].put(str1, ExifAttribute.createUShort(arrayOfInt1, this.mExifByteOrder));
                    i = j;
                    j = m;
                    break;
                  case 2:
                  case 7:
                    m = j;
                    j = i;
                    this.mAttributes[j].put(str1, ExifAttribute.createString((String)numberFormatException));
                    i = m;
                    break;
                  case 1:
                    m = i;
                    this.mAttributes[m].put(str1, ExifAttribute.createByte((String)numberFormatException));
                    i = j;
                    j = m;
                    break;
                } 
              } 
            } else {
              int m = j;
              j = i;
              i = m;
            } 
          } 
        } else {
          break;
        } 
        int k = j + 1;
        j = i;
      } 
      return;
    } 
    throw new NullPointerException("tag shouldn't be null");
  }
  
  public void setDateTime(long paramLong) {
    setAttribute("DateTime", sFormatter.format(new Date(paramLong)));
    setAttribute("SubSecTime", Long.toString(paramLong % 1000L));
  }
  
  public void setGpsInfo(Location paramLocation) {
    if (paramLocation == null)
      return; 
    setAttribute("GPSProcessingMethod", paramLocation.getProvider());
    setLatLong(paramLocation.getLatitude(), paramLocation.getLongitude());
    setAltitude(paramLocation.getAltitude());
    setAttribute("GPSSpeedRef", "K");
    setAttribute("GPSSpeed", (new Rational((paramLocation.getSpeed() * (float)TimeUnit.HOURS.toSeconds(1L) / 1000.0F))).toString());
    String[] arrayOfString = sFormatter.format(new Date(paramLocation.getTime())).split("\\s+", -1);
    setAttribute("GPSDateStamp", arrayOfString[0]);
    setAttribute("GPSTimeStamp", arrayOfString[1]);
  }
  
  public void setLatLong(double paramDouble1, double paramDouble2) {
    if (paramDouble1 >= -90.0D && paramDouble1 <= 90.0D && !Double.isNaN(paramDouble1)) {
      if (paramDouble2 >= -180.0D && paramDouble2 <= 180.0D && !Double.isNaN(paramDouble2)) {
        String str;
        if (paramDouble1 >= 0.0D) {
          str = "N";
        } else {
          str = "S";
        } 
        setAttribute("GPSLatitudeRef", str);
        setAttribute("GPSLatitude", convertDecimalDegree(Math.abs(paramDouble1)));
        if (paramDouble2 >= 0.0D) {
          str = "E";
        } else {
          str = "W";
        } 
        setAttribute("GPSLongitudeRef", str);
        setAttribute("GPSLongitude", convertDecimalDegree(Math.abs(paramDouble2)));
        return;
      } 
      throw new IllegalArgumentException("Longitude value " + paramDouble2 + " is not valid.");
    } 
    throw new IllegalArgumentException("Latitude value " + paramDouble1 + " is not valid.");
  }
  
  static {
    Integer integer4 = Integer.valueOf(3);
  }
  
  private static class ByteOrderedDataInputStream extends InputStream implements DataInput {
    private static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
    
    private static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
    
    private ByteOrder mByteOrder = ByteOrder.BIG_ENDIAN;
    
    private DataInputStream mDataInputStream;
    
    final int mLength;
    
    int mPosition;
    
    static {
    
    }
    
    public ByteOrderedDataInputStream(InputStream param1InputStream) throws IOException {
      this(param1InputStream, ByteOrder.BIG_ENDIAN);
    }
    
    ByteOrderedDataInputStream(InputStream param1InputStream, ByteOrder param1ByteOrder) throws IOException {
      param1InputStream = new DataInputStream(param1InputStream);
      this.mDataInputStream = (DataInputStream)param1InputStream;
      int i = param1InputStream.available();
      this.mLength = i;
      this.mPosition = 0;
      this.mDataInputStream.mark(i);
      this.mByteOrder = param1ByteOrder;
    }
    
    public ByteOrderedDataInputStream(byte[] param1ArrayOfbyte) throws IOException {
      this(new ByteArrayInputStream(param1ArrayOfbyte));
    }
    
    public int available() throws IOException {
      return this.mDataInputStream.available();
    }
    
    public int getLength() {
      return this.mLength;
    }
    
    public int peek() {
      return this.mPosition;
    }
    
    public int read() throws IOException {
      this.mPosition++;
      return this.mDataInputStream.read();
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      param1Int1 = this.mDataInputStream.read(param1ArrayOfbyte, param1Int1, param1Int2);
      this.mPosition += param1Int1;
      return param1Int1;
    }
    
    public boolean readBoolean() throws IOException {
      this.mPosition++;
      return this.mDataInputStream.readBoolean();
    }
    
    public byte readByte() throws IOException {
      int i = this.mPosition + 1;
      this.mPosition = i;
      if (i <= this.mLength) {
        i = this.mDataInputStream.read();
        if (i >= 0)
          return (byte)i; 
        throw new EOFException();
      } 
      throw new EOFException();
    }
    
    public char readChar() throws IOException {
      this.mPosition += 2;
      return this.mDataInputStream.readChar();
    }
    
    public double readDouble() throws IOException {
      return Double.longBitsToDouble(readLong());
    }
    
    public float readFloat() throws IOException {
      return Float.intBitsToFloat(readInt());
    }
    
    public void readFully(byte[] param1ArrayOfbyte) throws IOException {
      int i = this.mPosition + param1ArrayOfbyte.length;
      this.mPosition = i;
      if (i <= this.mLength) {
        if (this.mDataInputStream.read(param1ArrayOfbyte, 0, param1ArrayOfbyte.length) == param1ArrayOfbyte.length)
          return; 
        throw new IOException("Couldn't read up to the length of buffer");
      } 
      throw new EOFException();
    }
    
    public void readFully(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      int i = this.mPosition + param1Int2;
      this.mPosition = i;
      if (i <= this.mLength) {
        if (this.mDataInputStream.read(param1ArrayOfbyte, param1Int1, param1Int2) == param1Int2)
          return; 
        throw new IOException("Couldn't read up to the length of buffer");
      } 
      throw new EOFException();
    }
    
    public int readInt() throws IOException {
      int i = this.mPosition + 4;
      this.mPosition = i;
      if (i <= this.mLength) {
        int m = this.mDataInputStream.read();
        i = this.mDataInputStream.read();
        int j = this.mDataInputStream.read();
        int k = this.mDataInputStream.read();
        if ((m | i | j | k) >= 0) {
          ByteOrder byteOrder = this.mByteOrder;
          if (byteOrder == LITTLE_ENDIAN)
            return (k << 24) + (j << 16) + (i << 8) + m; 
          if (byteOrder == BIG_ENDIAN)
            return (m << 24) + (i << 16) + (j << 8) + k; 
          throw new IOException("Invalid byte order: " + this.mByteOrder);
        } 
        throw new EOFException();
      } 
      throw new EOFException();
    }
    
    public String readLine() throws IOException {
      Log.d("ExifInterface", "Currently unsupported");
      return null;
    }
    
    public long readLong() throws IOException {
      int i = this.mPosition + 8;
      this.mPosition = i;
      if (i <= this.mLength) {
        int n = this.mDataInputStream.read();
        int j = this.mDataInputStream.read();
        int i1 = this.mDataInputStream.read();
        i = this.mDataInputStream.read();
        int i3 = this.mDataInputStream.read();
        int i2 = this.mDataInputStream.read();
        int m = this.mDataInputStream.read();
        int k = this.mDataInputStream.read();
        if ((n | j | i1 | i | i3 | i2 | m | k) >= 0) {
          ByteOrder byteOrder = this.mByteOrder;
          if (byteOrder == LITTLE_ENDIAN)
            return (k << 56L) + (m << 48L) + (i2 << 40L) + (i3 << 32L) + (i << 24L) + (i1 << 16L) + (j << 8L) + n; 
          if (byteOrder == BIG_ENDIAN)
            return (n << 56L) + (j << 48L) + (i1 << 40L) + (i << 32L) + (i3 << 24L) + (i2 << 16L) + (m << 8L) + k; 
          throw new IOException("Invalid byte order: " + this.mByteOrder);
        } 
        throw new EOFException();
      } 
      throw new EOFException();
    }
    
    public short readShort() throws IOException {
      int i = this.mPosition + 2;
      this.mPosition = i;
      if (i <= this.mLength) {
        int j = this.mDataInputStream.read();
        i = this.mDataInputStream.read();
        if ((j | i) >= 0) {
          ByteOrder byteOrder = this.mByteOrder;
          if (byteOrder == LITTLE_ENDIAN)
            return (short)((i << 8) + j); 
          if (byteOrder == BIG_ENDIAN)
            return (short)((j << 8) + i); 
          throw new IOException("Invalid byte order: " + this.mByteOrder);
        } 
        throw new EOFException();
      } 
      throw new EOFException();
    }
    
    public String readUTF() throws IOException {
      this.mPosition += 2;
      return this.mDataInputStream.readUTF();
    }
    
    public int readUnsignedByte() throws IOException {
      this.mPosition++;
      return this.mDataInputStream.readUnsignedByte();
    }
    
    public long readUnsignedInt() throws IOException {
      return readInt() & 0xFFFFFFFFL;
    }
    
    public int readUnsignedShort() throws IOException {
      int i = this.mPosition + 2;
      this.mPosition = i;
      if (i <= this.mLength) {
        int j = this.mDataInputStream.read();
        i = this.mDataInputStream.read();
        if ((j | i) >= 0) {
          ByteOrder byteOrder = this.mByteOrder;
          if (byteOrder == LITTLE_ENDIAN)
            return (i << 8) + j; 
          if (byteOrder == BIG_ENDIAN)
            return (j << 8) + i; 
          throw new IOException("Invalid byte order: " + this.mByteOrder);
        } 
        throw new EOFException();
      } 
      throw new EOFException();
    }
    
    public void seek(long param1Long) throws IOException {
      int i = this.mPosition;
      if (i > param1Long) {
        this.mPosition = 0;
        this.mDataInputStream.reset();
        this.mDataInputStream.mark(this.mLength);
      } else {
        param1Long -= i;
      } 
      if (skipBytes((int)param1Long) == (int)param1Long)
        return; 
      throw new IOException("Couldn't seek up to the byteCount");
    }
    
    public void setByteOrder(ByteOrder param1ByteOrder) {
      this.mByteOrder = param1ByteOrder;
    }
    
    public int skipBytes(int param1Int) throws IOException {
      int i = Math.min(param1Int, this.mLength - this.mPosition);
      for (param1Int = 0; param1Int < i; param1Int += this.mDataInputStream.skipBytes(i - param1Int));
      this.mPosition += param1Int;
      return param1Int;
    }
  }
  
  private static class ByteOrderedDataOutputStream extends FilterOutputStream {
    private ByteOrder mByteOrder;
    
    final OutputStream mOutputStream;
    
    public ByteOrderedDataOutputStream(OutputStream param1OutputStream, ByteOrder param1ByteOrder) {
      super(param1OutputStream);
      this.mOutputStream = param1OutputStream;
      this.mByteOrder = param1ByteOrder;
    }
    
    public void setByteOrder(ByteOrder param1ByteOrder) {
      this.mByteOrder = param1ByteOrder;
    }
    
    public void write(byte[] param1ArrayOfbyte) throws IOException {
      this.mOutputStream.write(param1ArrayOfbyte);
    }
    
    public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      this.mOutputStream.write(param1ArrayOfbyte, param1Int1, param1Int2);
    }
    
    public void writeByte(int param1Int) throws IOException {
      this.mOutputStream.write(param1Int);
    }
    
    public void writeInt(int param1Int) throws IOException {
      if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
        this.mOutputStream.write(param1Int >>> 0 & 0xFF);
        this.mOutputStream.write(param1Int >>> 8 & 0xFF);
        this.mOutputStream.write(param1Int >>> 16 & 0xFF);
        this.mOutputStream.write(param1Int >>> 24 & 0xFF);
      } else if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
        this.mOutputStream.write(param1Int >>> 24 & 0xFF);
        this.mOutputStream.write(param1Int >>> 16 & 0xFF);
        this.mOutputStream.write(param1Int >>> 8 & 0xFF);
        this.mOutputStream.write(param1Int >>> 0 & 0xFF);
      } 
    }
    
    public void writeShort(short param1Short) throws IOException {
      if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
        this.mOutputStream.write(param1Short >>> 0 & 0xFF);
        this.mOutputStream.write(param1Short >>> 8 & 0xFF);
      } else if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
        this.mOutputStream.write(param1Short >>> 8 & 0xFF);
        this.mOutputStream.write(param1Short >>> 0 & 0xFF);
      } 
    }
    
    public void writeUnsignedInt(long param1Long) throws IOException {
      writeInt((int)param1Long);
    }
    
    public void writeUnsignedShort(int param1Int) throws IOException {
      writeShort((short)param1Int);
    }
  }
  
  private static class ExifAttribute {
    public static final long BYTES_OFFSET_UNKNOWN = -1L;
    
    public final byte[] bytes;
    
    public final long bytesOffset;
    
    public final int format;
    
    public final int numberOfComponents;
    
    ExifAttribute(int param1Int1, int param1Int2, long param1Long, byte[] param1ArrayOfbyte) {
      this.format = param1Int1;
      this.numberOfComponents = param1Int2;
      this.bytesOffset = param1Long;
      this.bytes = param1ArrayOfbyte;
    }
    
    ExifAttribute(int param1Int1, int param1Int2, byte[] param1ArrayOfbyte) {
      this(param1Int1, param1Int2, -1L, param1ArrayOfbyte);
    }
    
    public static ExifAttribute createByte(String param1String) {
      if (param1String.length() == 1 && param1String.charAt(0) >= '0' && param1String.charAt(0) <= '1') {
        byte[] arrayOfByte1 = new byte[1];
        arrayOfByte1[0] = (byte)(param1String.charAt(0) - 48);
        return new ExifAttribute(1, arrayOfByte1.length, arrayOfByte1);
      } 
      byte[] arrayOfByte = param1String.getBytes(ExifInterface.ASCII);
      return new ExifAttribute(1, arrayOfByte.length, arrayOfByte);
    }
    
    public static ExifAttribute createDouble(double param1Double, ByteOrder param1ByteOrder) {
      return createDouble(new double[] { param1Double }, param1ByteOrder);
    }
    
    public static ExifAttribute createDouble(double[] param1ArrayOfdouble, ByteOrder param1ByteOrder) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[12] * param1ArrayOfdouble.length]);
      byteBuffer.order(param1ByteOrder);
      int i = param1ArrayOfdouble.length;
      for (byte b = 0; b < i; b++)
        byteBuffer.putDouble(param1ArrayOfdouble[b]); 
      return new ExifAttribute(12, param1ArrayOfdouble.length, byteBuffer.array());
    }
    
    public static ExifAttribute createSLong(int param1Int, ByteOrder param1ByteOrder) {
      return createSLong(new int[] { param1Int }, param1ByteOrder);
    }
    
    public static ExifAttribute createSLong(int[] param1ArrayOfint, ByteOrder param1ByteOrder) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[9] * param1ArrayOfint.length]);
      byteBuffer.order(param1ByteOrder);
      int i = param1ArrayOfint.length;
      for (byte b = 0; b < i; b++)
        byteBuffer.putInt(param1ArrayOfint[b]); 
      return new ExifAttribute(9, param1ArrayOfint.length, byteBuffer.array());
    }
    
    public static ExifAttribute createSRational(ExifInterface.Rational param1Rational, ByteOrder param1ByteOrder) {
      return createSRational(new ExifInterface.Rational[] { param1Rational }, param1ByteOrder);
    }
    
    public static ExifAttribute createSRational(ExifInterface.Rational[] param1ArrayOfRational, ByteOrder param1ByteOrder) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[10] * param1ArrayOfRational.length]);
      byteBuffer.order(param1ByteOrder);
      int i = param1ArrayOfRational.length;
      for (byte b = 0; b < i; b++) {
        ExifInterface.Rational rational = param1ArrayOfRational[b];
        byteBuffer.putInt((int)rational.numerator);
        byteBuffer.putInt((int)rational.denominator);
      } 
      return new ExifAttribute(10, param1ArrayOfRational.length, byteBuffer.array());
    }
    
    public static ExifAttribute createString(String param1String) {
      byte[] arrayOfByte = (param1String + Character.MIN_VALUE).getBytes(ExifInterface.ASCII);
      return new ExifAttribute(2, arrayOfByte.length, arrayOfByte);
    }
    
    public static ExifAttribute createULong(long param1Long, ByteOrder param1ByteOrder) {
      return createULong(new long[] { param1Long }, param1ByteOrder);
    }
    
    public static ExifAttribute createULong(long[] param1ArrayOflong, ByteOrder param1ByteOrder) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * param1ArrayOflong.length]);
      byteBuffer.order(param1ByteOrder);
      int i = param1ArrayOflong.length;
      for (byte b = 0; b < i; b++)
        byteBuffer.putInt((int)param1ArrayOflong[b]); 
      return new ExifAttribute(4, param1ArrayOflong.length, byteBuffer.array());
    }
    
    public static ExifAttribute createURational(ExifInterface.Rational param1Rational, ByteOrder param1ByteOrder) {
      return createURational(new ExifInterface.Rational[] { param1Rational }, param1ByteOrder);
    }
    
    public static ExifAttribute createURational(ExifInterface.Rational[] param1ArrayOfRational, ByteOrder param1ByteOrder) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * param1ArrayOfRational.length]);
      byteBuffer.order(param1ByteOrder);
      int i = param1ArrayOfRational.length;
      for (byte b = 0; b < i; b++) {
        ExifInterface.Rational rational = param1ArrayOfRational[b];
        byteBuffer.putInt((int)rational.numerator);
        byteBuffer.putInt((int)rational.denominator);
      } 
      return new ExifAttribute(5, param1ArrayOfRational.length, byteBuffer.array());
    }
    
    public static ExifAttribute createUShort(int param1Int, ByteOrder param1ByteOrder) {
      return createUShort(new int[] { param1Int }, param1ByteOrder);
    }
    
    public static ExifAttribute createUShort(int[] param1ArrayOfint, ByteOrder param1ByteOrder) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * param1ArrayOfint.length]);
      byteBuffer.order(param1ByteOrder);
      int i = param1ArrayOfint.length;
      for (byte b = 0; b < i; b++)
        byteBuffer.putShort((short)param1ArrayOfint[b]); 
      return new ExifAttribute(3, param1ArrayOfint.length, byteBuffer.array());
    }
    
    public double getDoubleValue(ByteOrder param1ByteOrder) {
      Object object = getValue(param1ByteOrder);
      if (object != null) {
        if (object instanceof String)
          return Double.parseDouble((String)object); 
        if (object instanceof long[]) {
          object = object;
          if (object.length == 1)
            return object[0]; 
          throw new NumberFormatException("There are more than one component");
        } 
        if (object instanceof int[]) {
          object = object;
          if (object.length == 1)
            return object[0]; 
          throw new NumberFormatException("There are more than one component");
        } 
        if (object instanceof double[]) {
          object = object;
          if (object.length == 1)
            return object[0]; 
          throw new NumberFormatException("There are more than one component");
        } 
        if (object instanceof ExifInterface.Rational[]) {
          object = object;
          if (object.length == 1)
            return object[0].calculate(); 
          throw new NumberFormatException("There are more than one component");
        } 
        throw new NumberFormatException("Couldn't find a double value");
      } 
      throw new NumberFormatException("NULL can't be converted to a double value");
    }
    
    public int getIntValue(ByteOrder param1ByteOrder) {
      Object object = getValue(param1ByteOrder);
      if (object != null) {
        if (object instanceof String)
          return Integer.parseInt((String)object); 
        if (object instanceof long[]) {
          object = object;
          if (object.length == 1)
            return (int)object[0]; 
          throw new NumberFormatException("There are more than one component");
        } 
        if (object instanceof int[]) {
          object = object;
          if (object.length == 1)
            return object[0]; 
          throw new NumberFormatException("There are more than one component");
        } 
        throw new NumberFormatException("Couldn't find a integer value");
      } 
      throw new NumberFormatException("NULL can't be converted to a integer value");
    }
    
    public String getStringValue(ByteOrder param1ByteOrder) {
      Object object = getValue(param1ByteOrder);
      if (object == null)
        return null; 
      if (object instanceof String)
        return (String)object; 
      StringBuilder stringBuilder = new StringBuilder();
      if (object instanceof long[]) {
        object = object;
        for (byte b = 0; b < object.length; b++) {
          stringBuilder.append(object[b]);
          if (b + 1 != object.length)
            stringBuilder.append(","); 
        } 
        return stringBuilder.toString();
      } 
      if (object instanceof int[]) {
        object = object;
        for (byte b = 0; b < object.length; b++) {
          stringBuilder.append(object[b]);
          if (b + 1 != object.length)
            stringBuilder.append(","); 
        } 
        return stringBuilder.toString();
      } 
      if (object instanceof double[]) {
        object = object;
        for (byte b = 0; b < object.length; b++) {
          stringBuilder.append(object[b]);
          if (b + 1 != object.length)
            stringBuilder.append(","); 
        } 
        return stringBuilder.toString();
      } 
      if (object instanceof ExifInterface.Rational[]) {
        object = object;
        for (byte b = 0; b < object.length; b++) {
          stringBuilder.append(((ExifInterface.Rational)object[b]).numerator);
          stringBuilder.append('/');
          stringBuilder.append(((ExifInterface.Rational)object[b]).denominator);
          if (b + 1 != object.length)
            stringBuilder.append(","); 
        } 
        return stringBuilder.toString();
      } 
      return null;
    }
    
    Object getValue(ByteOrder param1ByteOrder) {
      // Byte code:
      //   0: aconst_null
      //   1: astore #8
      //   3: aconst_null
      //   4: astore #9
      //   6: aload #9
      //   8: astore #6
      //   10: aload #8
      //   12: astore #7
      //   14: new androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream
      //   17: astore #10
      //   19: aload #9
      //   21: astore #6
      //   23: aload #8
      //   25: astore #7
      //   27: aload #10
      //   29: aload_0
      //   30: getfield bytes : [B
      //   33: invokespecial <init> : ([B)V
      //   36: aload #10
      //   38: astore #8
      //   40: aload #8
      //   42: astore #6
      //   44: aload #8
      //   46: astore #7
      //   48: aload #8
      //   50: aload_1
      //   51: invokevirtual setByteOrder : (Ljava/nio/ByteOrder;)V
      //   54: aload #8
      //   56: astore #6
      //   58: aload #8
      //   60: astore #7
      //   62: aload_0
      //   63: getfield format : I
      //   66: tableswitch default -> 128, 1 -> 1020, 2 -> 776, 3 -> 699, 4 -> 622, 5 -> 532, 6 -> 1020, 7 -> 776, 8 -> 455, 9 -> 378, 10 -> 286, 11 -> 208, 12 -> 131
      //   128: goto -> 1159
      //   131: aload #8
      //   133: astore #6
      //   135: aload #8
      //   137: astore #7
      //   139: aload_0
      //   140: getfield numberOfComponents : I
      //   143: newarray double
      //   145: astore_1
      //   146: iconst_0
      //   147: istore_2
      //   148: aload #8
      //   150: astore #6
      //   152: aload #8
      //   154: astore #7
      //   156: iload_2
      //   157: aload_0
      //   158: getfield numberOfComponents : I
      //   161: if_icmpge -> 186
      //   164: aload #8
      //   166: astore #6
      //   168: aload #8
      //   170: astore #7
      //   172: aload_1
      //   173: iload_2
      //   174: aload #8
      //   176: invokevirtual readDouble : ()D
      //   179: dastore
      //   180: iinc #2, 1
      //   183: goto -> 148
      //   186: aload #8
      //   188: invokevirtual close : ()V
      //   191: goto -> 206
      //   194: astore #6
      //   196: ldc 'ExifInterface'
      //   198: ldc 'IOException occurred while closing InputStream'
      //   200: aload #6
      //   202: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   205: pop
      //   206: aload_1
      //   207: areturn
      //   208: aload #8
      //   210: astore #6
      //   212: aload #8
      //   214: astore #7
      //   216: aload_0
      //   217: getfield numberOfComponents : I
      //   220: newarray double
      //   222: astore_1
      //   223: iconst_0
      //   224: istore_2
      //   225: aload #8
      //   227: astore #6
      //   229: aload #8
      //   231: astore #7
      //   233: iload_2
      //   234: aload_0
      //   235: getfield numberOfComponents : I
      //   238: if_icmpge -> 264
      //   241: aload #8
      //   243: astore #6
      //   245: aload #8
      //   247: astore #7
      //   249: aload_1
      //   250: iload_2
      //   251: aload #8
      //   253: invokevirtual readFloat : ()F
      //   256: f2d
      //   257: dastore
      //   258: iinc #2, 1
      //   261: goto -> 225
      //   264: aload #8
      //   266: invokevirtual close : ()V
      //   269: goto -> 284
      //   272: astore #6
      //   274: ldc 'ExifInterface'
      //   276: ldc 'IOException occurred while closing InputStream'
      //   278: aload #6
      //   280: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   283: pop
      //   284: aload_1
      //   285: areturn
      //   286: aload #8
      //   288: astore #6
      //   290: aload #8
      //   292: astore #7
      //   294: aload_0
      //   295: getfield numberOfComponents : I
      //   298: anewarray androidx/exifinterface/media/ExifInterface$Rational
      //   301: astore_1
      //   302: iconst_0
      //   303: istore_2
      //   304: aload #8
      //   306: astore #6
      //   308: aload #8
      //   310: astore #7
      //   312: iload_2
      //   313: aload_0
      //   314: getfield numberOfComponents : I
      //   317: if_icmpge -> 356
      //   320: aload #8
      //   322: astore #6
      //   324: aload #8
      //   326: astore #7
      //   328: aload_1
      //   329: iload_2
      //   330: new androidx/exifinterface/media/ExifInterface$Rational
      //   333: dup
      //   334: aload #8
      //   336: invokevirtual readInt : ()I
      //   339: i2l
      //   340: aload #8
      //   342: invokevirtual readInt : ()I
      //   345: i2l
      //   346: invokespecial <init> : (JJ)V
      //   349: aastore
      //   350: iinc #2, 1
      //   353: goto -> 304
      //   356: aload #8
      //   358: invokevirtual close : ()V
      //   361: goto -> 376
      //   364: astore #6
      //   366: ldc 'ExifInterface'
      //   368: ldc 'IOException occurred while closing InputStream'
      //   370: aload #6
      //   372: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   375: pop
      //   376: aload_1
      //   377: areturn
      //   378: aload #8
      //   380: astore #6
      //   382: aload #8
      //   384: astore #7
      //   386: aload_0
      //   387: getfield numberOfComponents : I
      //   390: newarray int
      //   392: astore_1
      //   393: iconst_0
      //   394: istore_2
      //   395: aload #8
      //   397: astore #6
      //   399: aload #8
      //   401: astore #7
      //   403: iload_2
      //   404: aload_0
      //   405: getfield numberOfComponents : I
      //   408: if_icmpge -> 433
      //   411: aload #8
      //   413: astore #6
      //   415: aload #8
      //   417: astore #7
      //   419: aload_1
      //   420: iload_2
      //   421: aload #8
      //   423: invokevirtual readInt : ()I
      //   426: iastore
      //   427: iinc #2, 1
      //   430: goto -> 395
      //   433: aload #8
      //   435: invokevirtual close : ()V
      //   438: goto -> 453
      //   441: astore #6
      //   443: ldc 'ExifInterface'
      //   445: ldc 'IOException occurred while closing InputStream'
      //   447: aload #6
      //   449: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   452: pop
      //   453: aload_1
      //   454: areturn
      //   455: aload #8
      //   457: astore #6
      //   459: aload #8
      //   461: astore #7
      //   463: aload_0
      //   464: getfield numberOfComponents : I
      //   467: newarray int
      //   469: astore_1
      //   470: iconst_0
      //   471: istore_2
      //   472: aload #8
      //   474: astore #6
      //   476: aload #8
      //   478: astore #7
      //   480: iload_2
      //   481: aload_0
      //   482: getfield numberOfComponents : I
      //   485: if_icmpge -> 510
      //   488: aload #8
      //   490: astore #6
      //   492: aload #8
      //   494: astore #7
      //   496: aload_1
      //   497: iload_2
      //   498: aload #8
      //   500: invokevirtual readShort : ()S
      //   503: iastore
      //   504: iinc #2, 1
      //   507: goto -> 472
      //   510: aload #8
      //   512: invokevirtual close : ()V
      //   515: goto -> 530
      //   518: astore #6
      //   520: ldc 'ExifInterface'
      //   522: ldc 'IOException occurred while closing InputStream'
      //   524: aload #6
      //   526: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   529: pop
      //   530: aload_1
      //   531: areturn
      //   532: aload #8
      //   534: astore #6
      //   536: aload #8
      //   538: astore #7
      //   540: aload_0
      //   541: getfield numberOfComponents : I
      //   544: anewarray androidx/exifinterface/media/ExifInterface$Rational
      //   547: astore_1
      //   548: iconst_0
      //   549: istore_2
      //   550: aload #8
      //   552: astore #6
      //   554: aload #8
      //   556: astore #7
      //   558: iload_2
      //   559: aload_0
      //   560: getfield numberOfComponents : I
      //   563: if_icmpge -> 600
      //   566: aload #8
      //   568: astore #6
      //   570: aload #8
      //   572: astore #7
      //   574: aload_1
      //   575: iload_2
      //   576: new androidx/exifinterface/media/ExifInterface$Rational
      //   579: dup
      //   580: aload #8
      //   582: invokevirtual readUnsignedInt : ()J
      //   585: aload #8
      //   587: invokevirtual readUnsignedInt : ()J
      //   590: invokespecial <init> : (JJ)V
      //   593: aastore
      //   594: iinc #2, 1
      //   597: goto -> 550
      //   600: aload #8
      //   602: invokevirtual close : ()V
      //   605: goto -> 620
      //   608: astore #6
      //   610: ldc 'ExifInterface'
      //   612: ldc 'IOException occurred while closing InputStream'
      //   614: aload #6
      //   616: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   619: pop
      //   620: aload_1
      //   621: areturn
      //   622: aload #8
      //   624: astore #6
      //   626: aload #8
      //   628: astore #7
      //   630: aload_0
      //   631: getfield numberOfComponents : I
      //   634: newarray long
      //   636: astore_1
      //   637: iconst_0
      //   638: istore_2
      //   639: aload #8
      //   641: astore #6
      //   643: aload #8
      //   645: astore #7
      //   647: iload_2
      //   648: aload_0
      //   649: getfield numberOfComponents : I
      //   652: if_icmpge -> 677
      //   655: aload #8
      //   657: astore #6
      //   659: aload #8
      //   661: astore #7
      //   663: aload_1
      //   664: iload_2
      //   665: aload #8
      //   667: invokevirtual readUnsignedInt : ()J
      //   670: lastore
      //   671: iinc #2, 1
      //   674: goto -> 639
      //   677: aload #8
      //   679: invokevirtual close : ()V
      //   682: goto -> 697
      //   685: astore #6
      //   687: ldc 'ExifInterface'
      //   689: ldc 'IOException occurred while closing InputStream'
      //   691: aload #6
      //   693: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   696: pop
      //   697: aload_1
      //   698: areturn
      //   699: aload #8
      //   701: astore #6
      //   703: aload #8
      //   705: astore #7
      //   707: aload_0
      //   708: getfield numberOfComponents : I
      //   711: newarray int
      //   713: astore_1
      //   714: iconst_0
      //   715: istore_2
      //   716: aload #8
      //   718: astore #6
      //   720: aload #8
      //   722: astore #7
      //   724: iload_2
      //   725: aload_0
      //   726: getfield numberOfComponents : I
      //   729: if_icmpge -> 754
      //   732: aload #8
      //   734: astore #6
      //   736: aload #8
      //   738: astore #7
      //   740: aload_1
      //   741: iload_2
      //   742: aload #8
      //   744: invokevirtual readUnsignedShort : ()I
      //   747: iastore
      //   748: iinc #2, 1
      //   751: goto -> 716
      //   754: aload #8
      //   756: invokevirtual close : ()V
      //   759: goto -> 774
      //   762: astore #6
      //   764: ldc 'ExifInterface'
      //   766: ldc 'IOException occurred while closing InputStream'
      //   768: aload #6
      //   770: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   773: pop
      //   774: aload_1
      //   775: areturn
      //   776: iconst_0
      //   777: istore #4
      //   779: iload #4
      //   781: istore_2
      //   782: aload #8
      //   784: astore #6
      //   786: aload #8
      //   788: astore #7
      //   790: aload_0
      //   791: getfield numberOfComponents : I
      //   794: getstatic androidx/exifinterface/media/ExifInterface.EXIF_ASCII_PREFIX : [B
      //   797: arraylength
      //   798: if_icmplt -> 878
      //   801: iconst_1
      //   802: istore #5
      //   804: iconst_0
      //   805: istore_2
      //   806: iload #5
      //   808: istore_3
      //   809: aload #8
      //   811: astore #6
      //   813: aload #8
      //   815: astore #7
      //   817: iload_2
      //   818: getstatic androidx/exifinterface/media/ExifInterface.EXIF_ASCII_PREFIX : [B
      //   821: arraylength
      //   822: if_icmpge -> 858
      //   825: aload #8
      //   827: astore #6
      //   829: aload #8
      //   831: astore #7
      //   833: aload_0
      //   834: getfield bytes : [B
      //   837: iload_2
      //   838: baload
      //   839: getstatic androidx/exifinterface/media/ExifInterface.EXIF_ASCII_PREFIX : [B
      //   842: iload_2
      //   843: baload
      //   844: if_icmpeq -> 852
      //   847: iconst_0
      //   848: istore_3
      //   849: goto -> 858
      //   852: iinc #2, 1
      //   855: goto -> 806
      //   858: iload #4
      //   860: istore_2
      //   861: iload_3
      //   862: ifeq -> 878
      //   865: aload #8
      //   867: astore #6
      //   869: aload #8
      //   871: astore #7
      //   873: getstatic androidx/exifinterface/media/ExifInterface.EXIF_ASCII_PREFIX : [B
      //   876: arraylength
      //   877: istore_2
      //   878: aload #8
      //   880: astore #6
      //   882: aload #8
      //   884: astore #7
      //   886: new java/lang/StringBuilder
      //   889: astore_1
      //   890: aload #8
      //   892: astore #6
      //   894: aload #8
      //   896: astore #7
      //   898: aload_1
      //   899: invokespecial <init> : ()V
      //   902: aload #8
      //   904: astore #6
      //   906: aload #8
      //   908: astore #7
      //   910: iload_2
      //   911: aload_0
      //   912: getfield numberOfComponents : I
      //   915: if_icmpge -> 985
      //   918: aload #8
      //   920: astore #6
      //   922: aload #8
      //   924: astore #7
      //   926: aload_0
      //   927: getfield bytes : [B
      //   930: iload_2
      //   931: baload
      //   932: istore_3
      //   933: iload_3
      //   934: ifne -> 940
      //   937: goto -> 985
      //   940: iload_3
      //   941: bipush #32
      //   943: if_icmplt -> 964
      //   946: aload #8
      //   948: astore #6
      //   950: aload #8
      //   952: astore #7
      //   954: aload_1
      //   955: iload_3
      //   956: i2c
      //   957: invokevirtual append : (C)Ljava/lang/StringBuilder;
      //   960: pop
      //   961: goto -> 979
      //   964: aload #8
      //   966: astore #6
      //   968: aload #8
      //   970: astore #7
      //   972: aload_1
      //   973: bipush #63
      //   975: invokevirtual append : (C)Ljava/lang/StringBuilder;
      //   978: pop
      //   979: iinc #2, 1
      //   982: goto -> 902
      //   985: aload #8
      //   987: astore #6
      //   989: aload #8
      //   991: astore #7
      //   993: aload_1
      //   994: invokevirtual toString : ()Ljava/lang/String;
      //   997: astore_1
      //   998: aload #8
      //   1000: invokevirtual close : ()V
      //   1003: goto -> 1018
      //   1006: astore #6
      //   1008: ldc 'ExifInterface'
      //   1010: ldc 'IOException occurred while closing InputStream'
      //   1012: aload #6
      //   1014: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1017: pop
      //   1018: aload_1
      //   1019: areturn
      //   1020: aload #8
      //   1022: astore #6
      //   1024: aload #8
      //   1026: astore #7
      //   1028: aload_0
      //   1029: getfield bytes : [B
      //   1032: astore_1
      //   1033: aload #8
      //   1035: astore #6
      //   1037: aload #8
      //   1039: astore #7
      //   1041: aload_1
      //   1042: arraylength
      //   1043: iconst_1
      //   1044: if_icmpne -> 1114
      //   1047: aload_1
      //   1048: iconst_0
      //   1049: baload
      //   1050: iflt -> 1114
      //   1053: aload_1
      //   1054: iconst_0
      //   1055: baload
      //   1056: iconst_1
      //   1057: if_icmpgt -> 1114
      //   1060: aload #8
      //   1062: astore #6
      //   1064: aload #8
      //   1066: astore #7
      //   1068: new java/lang/String
      //   1071: dup
      //   1072: iconst_1
      //   1073: newarray char
      //   1075: dup
      //   1076: iconst_0
      //   1077: aload_0
      //   1078: getfield bytes : [B
      //   1081: iconst_0
      //   1082: baload
      //   1083: bipush #48
      //   1085: iadd
      //   1086: i2c
      //   1087: castore
      //   1088: invokespecial <init> : ([C)V
      //   1091: astore_1
      //   1092: aload #8
      //   1094: invokevirtual close : ()V
      //   1097: goto -> 1112
      //   1100: astore #6
      //   1102: ldc 'ExifInterface'
      //   1104: ldc 'IOException occurred while closing InputStream'
      //   1106: aload #6
      //   1108: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1111: pop
      //   1112: aload_1
      //   1113: areturn
      //   1114: aload #8
      //   1116: astore #6
      //   1118: aload #8
      //   1120: astore #7
      //   1122: new java/lang/String
      //   1125: dup
      //   1126: aload_0
      //   1127: getfield bytes : [B
      //   1130: getstatic androidx/exifinterface/media/ExifInterface.ASCII : Ljava/nio/charset/Charset;
      //   1133: invokespecial <init> : ([BLjava/nio/charset/Charset;)V
      //   1136: astore_1
      //   1137: aload #8
      //   1139: invokevirtual close : ()V
      //   1142: goto -> 1157
      //   1145: astore #6
      //   1147: ldc 'ExifInterface'
      //   1149: ldc 'IOException occurred while closing InputStream'
      //   1151: aload #6
      //   1153: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1156: pop
      //   1157: aload_1
      //   1158: areturn
      //   1159: aload #8
      //   1161: invokevirtual close : ()V
      //   1164: goto -> 1177
      //   1167: astore_1
      //   1168: ldc 'ExifInterface'
      //   1170: ldc 'IOException occurred while closing InputStream'
      //   1172: aload_1
      //   1173: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1176: pop
      //   1177: aconst_null
      //   1178: areturn
      //   1179: astore_1
      //   1180: goto -> 1222
      //   1183: astore_1
      //   1184: aload #7
      //   1186: astore #6
      //   1188: ldc 'ExifInterface'
      //   1190: ldc 'IOException occurred during reading a value'
      //   1192: aload_1
      //   1193: invokestatic w : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1196: pop
      //   1197: aload #7
      //   1199: ifnull -> 1220
      //   1202: aload #7
      //   1204: invokevirtual close : ()V
      //   1207: goto -> 1220
      //   1210: astore_1
      //   1211: ldc 'ExifInterface'
      //   1213: ldc 'IOException occurred while closing InputStream'
      //   1215: aload_1
      //   1216: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1219: pop
      //   1220: aconst_null
      //   1221: areturn
      //   1222: aload #6
      //   1224: ifnull -> 1247
      //   1227: aload #6
      //   1229: invokevirtual close : ()V
      //   1232: goto -> 1247
      //   1235: astore #6
      //   1237: ldc 'ExifInterface'
      //   1239: ldc 'IOException occurred while closing InputStream'
      //   1241: aload #6
      //   1243: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1246: pop
      //   1247: aload_1
      //   1248: athrow
      // Exception table:
      //   from	to	target	type
      //   14	19	1183	java/io/IOException
      //   14	19	1179	finally
      //   27	36	1183	java/io/IOException
      //   27	36	1179	finally
      //   48	54	1183	java/io/IOException
      //   48	54	1179	finally
      //   62	128	1183	java/io/IOException
      //   62	128	1179	finally
      //   139	146	1183	java/io/IOException
      //   139	146	1179	finally
      //   156	164	1183	java/io/IOException
      //   156	164	1179	finally
      //   172	180	1183	java/io/IOException
      //   172	180	1179	finally
      //   186	191	194	java/io/IOException
      //   216	223	1183	java/io/IOException
      //   216	223	1179	finally
      //   233	241	1183	java/io/IOException
      //   233	241	1179	finally
      //   249	258	1183	java/io/IOException
      //   249	258	1179	finally
      //   264	269	272	java/io/IOException
      //   294	302	1183	java/io/IOException
      //   294	302	1179	finally
      //   312	320	1183	java/io/IOException
      //   312	320	1179	finally
      //   328	350	1183	java/io/IOException
      //   328	350	1179	finally
      //   356	361	364	java/io/IOException
      //   386	393	1183	java/io/IOException
      //   386	393	1179	finally
      //   403	411	1183	java/io/IOException
      //   403	411	1179	finally
      //   419	427	1183	java/io/IOException
      //   419	427	1179	finally
      //   433	438	441	java/io/IOException
      //   463	470	1183	java/io/IOException
      //   463	470	1179	finally
      //   480	488	1183	java/io/IOException
      //   480	488	1179	finally
      //   496	504	1183	java/io/IOException
      //   496	504	1179	finally
      //   510	515	518	java/io/IOException
      //   540	548	1183	java/io/IOException
      //   540	548	1179	finally
      //   558	566	1183	java/io/IOException
      //   558	566	1179	finally
      //   574	594	1183	java/io/IOException
      //   574	594	1179	finally
      //   600	605	608	java/io/IOException
      //   630	637	1183	java/io/IOException
      //   630	637	1179	finally
      //   647	655	1183	java/io/IOException
      //   647	655	1179	finally
      //   663	671	1183	java/io/IOException
      //   663	671	1179	finally
      //   677	682	685	java/io/IOException
      //   707	714	1183	java/io/IOException
      //   707	714	1179	finally
      //   724	732	1183	java/io/IOException
      //   724	732	1179	finally
      //   740	748	1183	java/io/IOException
      //   740	748	1179	finally
      //   754	759	762	java/io/IOException
      //   790	801	1183	java/io/IOException
      //   790	801	1179	finally
      //   817	825	1183	java/io/IOException
      //   817	825	1179	finally
      //   833	847	1183	java/io/IOException
      //   833	847	1179	finally
      //   873	878	1183	java/io/IOException
      //   873	878	1179	finally
      //   886	890	1183	java/io/IOException
      //   886	890	1179	finally
      //   898	902	1183	java/io/IOException
      //   898	902	1179	finally
      //   910	918	1183	java/io/IOException
      //   910	918	1179	finally
      //   926	933	1183	java/io/IOException
      //   926	933	1179	finally
      //   954	961	1183	java/io/IOException
      //   954	961	1179	finally
      //   972	979	1183	java/io/IOException
      //   972	979	1179	finally
      //   993	998	1183	java/io/IOException
      //   993	998	1179	finally
      //   998	1003	1006	java/io/IOException
      //   1028	1033	1183	java/io/IOException
      //   1028	1033	1179	finally
      //   1041	1047	1183	java/io/IOException
      //   1041	1047	1179	finally
      //   1068	1092	1183	java/io/IOException
      //   1068	1092	1179	finally
      //   1092	1097	1100	java/io/IOException
      //   1122	1137	1183	java/io/IOException
      //   1122	1137	1179	finally
      //   1137	1142	1145	java/io/IOException
      //   1159	1164	1167	java/io/IOException
      //   1188	1197	1179	finally
      //   1202	1207	1210	java/io/IOException
      //   1227	1232	1235	java/io/IOException
    }
    
    public int size() {
      return ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[this.format] * this.numberOfComponents;
    }
    
    public String toString() {
      return "(" + ExifInterface.IFD_FORMAT_NAMES[this.format] + ", data length:" + this.bytes.length + ")";
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ExifStreamType {}
  
  static class ExifTag {
    public final String name;
    
    public final int number;
    
    public final int primaryFormat;
    
    public final int secondaryFormat;
    
    ExifTag(String param1String, int param1Int1, int param1Int2) {
      this.name = param1String;
      this.number = param1Int1;
      this.primaryFormat = param1Int2;
      this.secondaryFormat = -1;
    }
    
    ExifTag(String param1String, int param1Int1, int param1Int2, int param1Int3) {
      this.name = param1String;
      this.number = param1Int1;
      this.primaryFormat = param1Int2;
      this.secondaryFormat = param1Int3;
    }
    
    boolean isFormatCompatible(int param1Int) {
      int i = this.primaryFormat;
      if (i == 7 || param1Int == 7)
        return true; 
      if (i != param1Int) {
        int j = this.secondaryFormat;
        if (j != param1Int)
          return ((i == 4 || j == 4) && param1Int == 3) ? true : (((i == 9 || j == 9) && param1Int == 8) ? true : (((i == 12 || j == 12) && param1Int == 11))); 
      } 
      return true;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface IfdType {}
  
  private static class Rational {
    public final long denominator;
    
    public final long numerator;
    
    Rational(double param1Double) {
      this((long)(10000.0D * param1Double), 10000L);
    }
    
    Rational(long param1Long1, long param1Long2) {
      if (param1Long2 == 0L) {
        this.numerator = 0L;
        this.denominator = 1L;
        return;
      } 
      this.numerator = param1Long1;
      this.denominator = param1Long2;
    }
    
    public double calculate() {
      return this.numerator / this.denominator;
    }
    
    public String toString() {
      return this.numerator + "/" + this.denominator;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\exifinterface\media\ExifInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */