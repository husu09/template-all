package com.su.akka.iot;

import java.util.Optional;

import com.su.akka.iot.DeviceManager.DeviceRegistered;
import com.su.akka.iot.DeviceManager.RequestTrackDevice;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Device extends AbstractActor {
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	//	组id
	final String groupId;
	// 设备id
	final String deviceId;

	public Device(String groupId, String deviceId) {
		this.groupId = groupId;
		this.deviceId = deviceId;
	}

	public static Props props(String groupId, String deviceId) {
		return Props.create(Device.class, groupId, deviceId);
	}
	
	// 记录温度协议
	public static final class RecordTemperature {
		final long requestId;
		final double value;

		public RecordTemperature(long requestId, double value) {
			this.requestId = requestId;
			this.value = value;
		}
	}

	public static final class TemperatureRecorded {
		final long requestId;

		public TemperatureRecorded(long requestId) {
			this.requestId = requestId;
		}
	}
	// 读取温度协议
	public static final class ReadTemperature {
		final long requestId;

		public ReadTemperature(long requestId) {
			this.requestId = requestId;
		}
	}

	public static final class RespondTemperature {
		final long requestId;
		final Optional<Double> value;

		public RespondTemperature(long requestId, Optional<Double> value) {
			this.requestId = requestId;
			this.value = value;
		}
	}
	// 保存温度
	Optional<Double> lastTemperatureReading = Optional.empty();

	@Override
	public void preStart() {
		log.info("Device actor {}-{} started", groupId, deviceId);
	}

	@Override
	public void postStop() {
		log.info("Device actor {}-{} stopped", groupId, deviceId);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(RequestTrackDevice.class, r -> {
			if (this.groupId.equals(r.groupId) && this.deviceId.equals(r.deviceId)) {
				getSender().tell(new DeviceRegistered(), getSelf());
			} else {
				log.warning("Ignoring TrackDevice request for {}-{}.This actor is responsible for {}-{}.", r.groupId,
						r.deviceId, this.groupId, this.deviceId);
			}
		}).match(RecordTemperature.class, r -> {
			// 记录温度
			log.info("Recorded temperature reading {} with {}", r.value, r.requestId);
			lastTemperatureReading = Optional.of(r.value);
			getSender().tell(new TemperatureRecorded(r.requestId), getSelf());
		}).match(ReadTemperature.class, r -> {
			// 读取温度
			getSender().tell(new RespondTemperature(r.requestId, lastTemperatureReading), getSelf());
		}).build();
	}
}