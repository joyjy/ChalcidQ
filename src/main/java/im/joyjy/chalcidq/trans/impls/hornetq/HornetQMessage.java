package im.joyjy.chalcidq.trans.impls.hornetq;

import java.nio.charset.StandardCharsets;

import im.joyjy.chalcidq.trans.Packet;
import io.netty.buffer.ByteBuf;

public abstract class HornetQMessage extends Packet {

	public static final int HEADER_SIZE = 4 + 1 + 8;

	public static final byte PING = 10;
	public static final byte EXCEPTION = 20;
	public static final byte NULL_RESPONSE = 21;
	public static final byte CREATESESSION = 30;
	public static final byte CREATESESSION_RESP = 31;
	public static final byte CREATE_QUEUE = 34;
	public static final byte SESS_CREATESONCUMER = 40;
	public static final byte SESS_QUEUEQUEUY_RESP = 46;
	public static final byte SESS_START = 67;
	public static final byte SESS_FLOWTOKEN = 70;
	public static final byte SESS_RECEIVE_MSG = 75;
	public static final byte SESS_INDIVIDUAL_ACKNOWLEDGE = 81;
	public static final byte SUSCRIBE_TOPOLOGY_V2 = 113;
	public static final byte CLUSTER_TOPOLOGY_V2 = 114;

	/**
	 * 写入字符串
	 * 
	 * @param buf
	 * @param data
	 */
	public static void writeString(ByteBuf buf, String data) {
		byte[] bytes = data.getBytes();
		buf.writeInt(bytes.length);
		buf.writeShort(bytes.length);
		buf.writeBytes(bytes);
	}

	/**
	 * 读取字符串（包含是否为空字节）
	 * 
	 * @param in
	 * @return
	 */
	public static String readNullableString(ByteBuf in) {
		int b = in.readByte();
		if (b == 0) {
			return null;
		}

		return readString(in);
	}

	/**
	 * 读取字符串（不包含是否为空字节）
	 * @param in
	 * @return
	 */
	public static String readString(ByteBuf in) {
		int len = in.readInt();
		if (len < 9) {
			char[] chars = new char[len];
			for (int i = 0; i < len; i++) {
				chars[i] = in.readChar();
			}
			return new String(chars);
		} else if (len < 0xfff) {
			int size = in.readUnsignedShort();
			byte[] bytes = new byte[size];
			in.readBytes(bytes);
			return new String(bytes, StandardCharsets.UTF_8);
		} else {
			return readSimpleString(in);
		}
	}

	/**
	 * 写入简单字符串
	 * @param buf
	 * @param value
	 */
	public static void writeSimpleString(ByteBuf buf, String value) {
		buf.writeInt(value.length() * 2);
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);

			byte low = (byte) (c & 0xFF); // low byte
			buf.writeByte(low);

			byte high = (byte) (c >> 8 & 0xFF); // high byte
			buf.writeByte(high);
		}
	}
	
	/**
	 * 读取简单字符串（包含是否为空字节）
	 * @param in
	 * @return
	 */
	public static String readNullableSimpleString(ByteBuf in) {
	      int b = in.readByte();
	      if (b == 0)
	      {
	         return null;
	      }
	      return readSimpleString(in);
	}

	/**
	 * 读取简单字符串（不包含是否为空字节）
	 * @param in
	 * @return
	 */
	public static String readSimpleString(ByteBuf in) {	
		int len = in.readInt();
        char[] chars = new char[len >> 1];
        for (int i = 0; i < chars.length; i++)
        {
           int low = in.readByte() & 0xFF;
           int high = in.readByte() << 8 & 0xFF00;
           chars[i] = (char)(low | high);
        }
		return new String(chars);
	}

	private byte type;
	private int size;

	/**
	 * 发送此消息的 ChannelId
	 */
	protected long channelId;

	/**
	 * 创建 HornetQ 消息
	 * @param type
	 */
	public HornetQMessage(byte type) {
		this.type = type;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public boolean isResponse() {
		return false;
	}

	@Override
	public void encode(ByteBuf buf) {
		
		buf.writeInt(0);
		buf.writeByte(type);
		buf.writeLong(channelId);
		
		encodeBody(buf);
		
		size = buf.writerIndex();
		buf.setInt(0, size - 4);
	}

	/**
	 * 编码消息体
	 * @param buf
	 */
	public abstract void encodeBody(ByteBuf buf);

	/**
	 * 解码消息头
	 * @param in
	 */
	public void decode(ByteBuf in) {
		// 传入的时候 size 和 type 均已被读取
		channelId = in.readLong();
	}
}
