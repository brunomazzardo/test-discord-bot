package kynake.discord.bot.audio;

// JDA
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;

// Java
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.nio.ByteBuffer;

public class AudioTester implements AudioReceiveHandler, AudioSendHandler { // Test recording by echoing
  /** TESTING */
  private Queue<byte[]> buffer = new ConcurrentLinkedQueue<>(); // buffer of byte chunks of 20ms of user Audio
  private long requesterID;

  public AudioTester(User requester) {
    requesterID = requester.getIdLong();
  }

  // Receiving
  @Override
  public boolean canReceiveUser() {
    return true;
  }

  @Override
  public void handleUserAudio(UserAudio userAudio) {

    // Only record Audio from the user who called the bot to the Voice Channel
    if(userAudio.getUser().getIdLong() != requesterID) {
      return;
    }

    byte[] data = userAudio.getAudioData(1.0f);
    buffer.add(data);
  }


  /** TESTING */
  // Sending
  @Override
  public boolean canProvide() {
    // Audio can be provided if the buffer is not empty
    return !buffer.isEmpty();
  }

  @Override
  public ByteBuffer provide20MsAudio() {
    // use what we have in our buffer to send audio as PCM
    byte[] data = buffer.poll();
    return data == null ? null : ByteBuffer.wrap(data); // Wrap this in a java.nio.ByteBuffer
  }
}
