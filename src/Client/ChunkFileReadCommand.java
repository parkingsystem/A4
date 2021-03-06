package Client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChunkFileReadCommand implements Command {

	public static final String cmd = "CMD_CHUNK_NODE_TO_CLIENT_FILE_INFO";

	public String ipAddress;
	public int port;
	public String clientIP;
	public int clientPORT;
	public String fileName;

	public ChunkFileReadCommand() {
	}

	public ChunkFileReadCommand(String ipAddress, int port,String clientIP,int clientPORT, String fileName) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
		this.clientIP = clientIP;
		this.clientPORT = clientPORT;
		this.fileName = fileName;

	}

	public byte[] unpack() {

		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = null;
		DataOutputStream dout = null;

		try {
			baOutputStream = new ByteArrayOutputStream();
			dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
			dout.writeInt(cmd.length());
			dout.write(cmd.getBytes());
			dout.writeInt(ipAddress.length());
			dout.write(ipAddress.getBytes());
			dout.writeInt(port);
			dout.writeInt(clientIP.length());
			dout.write(clientIP.getBytes());
			dout.writeInt(clientPORT);
			dout.writeInt(fileName.length());
			dout.write(fileName.getBytes());
			dout.flush();
			marshalledBytes = baOutputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baOutputStream.close();
				dout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return marshalledBytes;
	}

	public void pack(DataInputStream din) {
		try {

			ipAddress = readString(din);
			port = din.readInt();
			clientIP=readString(din);
			clientPORT= din.readInt();
			fileName = readString(din);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String readString(DataInputStream din) throws IOException {
		int IP_length = din.readInt();
		byte[] IP_address = new byte[IP_length];
		din.readFully(IP_address);
		return new String(IP_address);
	}

	@Override
	public String toString() {
		return "ChunkNodeFileInfoCommand [cmd=" + cmd + ", ipAddress=" + ipAddress + ", port=" + port + "]";
	}

}
