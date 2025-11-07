/*
이 파일은 Spring Integration을 사용하여 TCP 서버를 설정하고 실행하는 코드

AbstractServerConnectionFactory:
9090 포트를 열고 연결을 관리하는 메서드

TcpInboundGateway:
AbstractServerConnectionFactory로 들어온 요청을
tcpRequestChannel이라는 내부 처리 라인으로 보내고,
응답이 올 때까지 기다렸다가 클라이언트에게 다시 전송하는 메서드

ByteArrayLfSerializer:
클라이언트와 문자를 기준으로 메시지 하나가 끝났다고 약속하는 통신 규약
 */

package kr.co.ap.flobankap.tcp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNioServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLfSerializer;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.channel.DirectChannel;

@Configuration
@EnableIntegration
@IntegrationComponentScan("kr.co.ap.flobankap.tcp.service") // TcpHandlerService를 찾기 위한 스캔
public class TcpServerConfig {

    // application.yml에서 포트 번호(9090)를 가져옴
    @Value("${flobank.ap.port}")
    private int port; // == 9090

    /**
     * 1. TCP 서버 연결 팩토리
     * - 9090 포트를 열고 연결을 수신 대기
     */
    @Bean
    public AbstractServerConnectionFactory serverConnectionFactory() {
        TcpNioServerConnectionFactory factory = new TcpNioServerConnectionFactory(port);

        // 2. Serializer / Deserializer 설정
        // - 데이터를 주고받을 때 줄바꿈(\n)을 기준으로 하나의 메시지로 인식
        // - 클라이언트(flobank_api)와 반드시 동일해야함
        ByteArrayLfSerializer serializer = new ByteArrayLfSerializer();
        factory.setSerializer(serializer);
        factory.setDeserializer(serializer);

        // (중요) 단일 클라이언트(api)가 아니라 여러 클라이언트가 동시 접속할 경우를 대비
        factory.setSingleUse(false);

        return factory;
    }

    /**
     * 3. TCP 인바운드 게이트웨이 (접수 창구)
     * - ConnectionFactory(교환기)로 들어온 연결을 받아 처리
     * - 요청을 "tcpRequestChannel"로 보내고, 응답을 기다렸다가 클라이언트에 전송
     */
    @Bean
    public TcpInboundGateway tcpInboundGateway(AbstractServerConnectionFactory serverConnectionFactory) {
        TcpInboundGateway gateway = new TcpInboundGateway();
        gateway.setConnectionFactory(serverConnectionFactory);
        gateway.setRequestChannel(tcpRequestChannel()); // 요청을 보낼 채널
        // reply-channel은 설정하지 않아도, 요청 채널에서 응답이 오면 자동으로 보내짐
        return gateway;
    }

    /**
     * 4. 요청 메시지 채널 (내부 처리 라인)
     * - 'tcpInboundGateway'가 받은 메시지를 이 채널로 보냄
     * - 'TcpHandlerService'가 이 채널을 구독(listening)
     */
    @Bean
    public MessageChannel tcpRequestChannel() {
        return new DirectChannel();
    }
}