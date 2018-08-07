/*
 * Copyright 2007-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.framework.commons;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/22 下午7:02
 * @version 1.0
 */
public class FFmpegHelper {

    private static final Log _LOG = LogFactory.getLog(FFmpegHelper.class);

    private String __ffmpegPath;

    private String __mediaFile;

    public static FFmpegHelper create(String ffmpegPath) {
        return new FFmpegHelper(ffmpegPath);
    }

    private FFmpegHelper(String ffmpegPath) {
        if (StringUtils.isBlank(ffmpegPath) && __doCheckFile(ffmpegPath)) {
            throw new IllegalArgumentException("Argument ffmpegPath illegal.");
        }
        __ffmpegPath = ffmpegPath;
    }

    public FFmpegHelper bind(String mediaFile) {
        if (!__doCheckFile(mediaFile) /* || !__doCheckContentType(FileUtils.getExtName(videoFile)) */) {
            throw new IllegalArgumentException("Argument mediaFile illegal.");
        }
        __mediaFile = mediaFile;
        return this;
    }

    public FFmpegHelper bind(File mediaFile) {
        return bind(mediaFile == null ? null : mediaFile.getPath());
    }

//    private boolean __doCheckContentType(String type) {
//        Pattern pattern = Pattern.compile("(asx|asf|mpg|wmv|3gp|mp4|mov|avi|flv)$", Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(type);
//        return matcher.find();
//    }

    private boolean __doCheckFile(String file) {
        File _file = new File(file);
        return _file.exists() && _file.isFile();
    }

    private int __doGetTimeLength(String timelen) {
        int min = 0;
        String[] _strArr = timelen.split(":");
        if (_strArr[0].compareTo("0") > 0) {
            min += Integer.valueOf(_strArr[0]) * 60 * 60;
        }
        if (_strArr[1].compareTo("0") > 0) {
            min += Integer.valueOf(_strArr[1]) * 60;
        }
        if (_strArr[2].compareTo("0") > 0) {
            min += Math.round(Float.valueOf(_strArr[2]));
        }
        return min;
    }

    public MediaInfo getMediaInfo() {
        try {
            String _outputStr = ConsoleCmdExecutor.exec(__ffmpegPath, "-i", __mediaFile);
            _LOG.info(_outputStr);
            //从视频信息中解析时长
            String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
            String regexVideo = "Video: (.*?), (.*?), (.*?)[,\\s]";
            String regexAudio = "Audio: (\\w*), (\\d*) Hz";
            //
            MediaInfo _info = new MediaInfo();
            //
            Pattern pattern = Pattern.compile(regexDuration);
            Matcher m = pattern.matcher(_outputStr);
            if (m.find()) {
                _info.setStart(m.group(2));
                _info.setBitrates(m.group(3));
                _info.setTime(__doGetTimeLength(m.group(1)));
            }
            pattern = Pattern.compile(regexVideo);
            m = pattern.matcher(_outputStr);
            if (m.find()) {
                _info.setVideoEncodingFormat(m.group(1));
                _info.setVideoFormat(m.group(2));
                _info.setResolution(m.group(3));
            }
            pattern = Pattern.compile(regexAudio);
            m = pattern.matcher(_outputStr);
            if (m.find()) {
                _info.setAudioEncodingFormat(m.group(1));
                _info.setAudioSamplingRate(m.group(2));
            }
            return _info;
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public String audioToMP3(File outputMP3) {
        try {
            ConsoleCmdExecutor.exec(new String[]{__ffmpegPath, "-i", __mediaFile, outputMP3.getPath()}, new WriteConsoleLog());
            return outputMP3.getPath();
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public String screenshotVideo(String startSecond, String imageSize, File outputJPG) {
        List<String> _cmd = new ArrayList<String>();
        _cmd.add(__ffmpegPath);
        _cmd.add("-i");
        _cmd.add(__mediaFile);
        _cmd.add("-y");
        _cmd.add("-f");
        _cmd.add("mjpeg");
        if (StringUtils.isNotBlank(startSecond)) {
            // 设置截取视频画面时间
            _cmd.add("-ss");
            _cmd.add(startSecond);
        }
        _cmd.add("-t");
        _cmd.add("0.001");
        if (StringUtils.isNotBlank(imageSize)) {
            // 设置截图大小
            _cmd.add("-s");
            _cmd.add(imageSize);
        }
        _cmd.add(outputJPG.getPath());
        //
        try {
            ConsoleCmdExecutor.exec(_cmd, new WriteConsoleLog());
            return outputJPG.getPath();
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public String videoToFLV(String imageSize, File outputFLV) {
        List<String> _cmd = new ArrayList<String>();
        _cmd.add(__ffmpegPath);
        _cmd.add("-y");
        _cmd.add("-i");
        _cmd.add(__mediaFile);
        _cmd.add("-ab");
        _cmd.add("56");
        _cmd.add("-ar");
        _cmd.add("22050");
        _cmd.add("-b");
        _cmd.add("500");
        if (StringUtils.isNotBlank(imageSize)) {
            _cmd.add("-s");
            _cmd.add(imageSize);
        }
        _cmd.add(outputFLV.getPath());
        //
        try {
            ConsoleCmdExecutor.exec(_cmd, new WriteConsoleLog());
            return outputFLV.getPath();
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    class WriteConsoleLog implements ICmdOutputHandler<Void> {
        @Override
        public Void handle(BufferedReader reader) throws Exception {
            String _line = null;
            while ((_line = reader.readLine()) != null) {
                _LOG.info(_line);
            }
            return null;
        }
    }

    public static class MediaInfo implements Serializable {
        private String start;
        private String bitrates;
        private int time;

        private String videoEncodingFormat;
        private String videoFormat;
        private String resolution;

        private String audioEncodingFormat;
        private String audioSamplingRate;

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getBitrates() {
            return bitrates;
        }

        public void setBitrates(String bitrates) {
            this.bitrates = bitrates;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getVideoEncodingFormat() {
            return videoEncodingFormat;
        }

        public void setVideoEncodingFormat(String videoEncodingFormat) {
            this.videoEncodingFormat = videoEncodingFormat;
        }

        public String getVideoFormat() {
            return videoFormat;
        }

        public void setVideoFormat(String videoFormat) {
            this.videoFormat = videoFormat;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getAudioEncodingFormat() {
            return audioEncodingFormat;
        }

        public void setAudioEncodingFormat(String audioEncodingFormat) {
            this.audioEncodingFormat = audioEncodingFormat;
        }

        public String getAudioSamplingRate() {
            return audioSamplingRate;
        }

        public void setAudioSamplingRate(String audioSamplingRate) {
            this.audioSamplingRate = audioSamplingRate;
        }
    }
}
