package its_meow.mcmusicplayer;

import java.io.File;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;



public class AudioConverter {

    private static final Integer bitrate = 256000;
    private static final Integer channels = 2; //2 for stereo, 1 for mono
    private static final Integer samplingRate = 96000;

    /* Data structures for the audio 
     *  and Encoding attributes
     */
    private AudioAttributes audioAttr = new AudioAttributes();
    private EncodingAttributes encoAttrs = new EncodingAttributes();
    private Encoder encoder = new Encoder();

    /*
     * File formats that will be converted 
     * Please Don't change!
     */
    private String oggFormat = "ogg";
    private String mp3Format = "mp3";
    private String wavFormat = "wav";

    /*
     * Codecs to be used
     */
    private String oggCodec = "vorbis";


    /* Set the default attributes
     * for encoding
     */
    public AudioConverter(){
        audioAttr.setBitRate(bitrate);
        audioAttr.setChannels(channels);
        audioAttr.setSamplingRate(samplingRate);
    }

    public void encodeAudio(File source, File target, String mimeType){
        //Change the hardcoded mime type later on
        if(mimeType.equals("audio/mp3")){
            this.mp3ToOgg(source, target);
        }
    }

    private void mp3ToOgg(File source, File target){
        //ADD CODE FOR CHANGING THE EXTENSION OF THE FILE
        encoAttrs.setFormat(oggFormat);
        audioAttr.setCodec(oggCodec);
        encoAttrs.setAudioAttributes(audioAttr);
        try{
        encoder.encode(source, target, encoAttrs);
        }catch(Exception e){
            System.out.println("Encoding Failed");
        }
    }

}