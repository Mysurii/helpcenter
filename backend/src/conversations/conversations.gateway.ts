import {
  WebSocketGateway,
  SubscribeMessage,
  MessageBody,
  WebSocketServer,
} from '@nestjs/websockets';
import { ConversationsService } from './conversations.service';
import { CreateConversationDto } from './dto/create-conversation.dto';
import { UpdateConversationDto } from './dto/update-conversation.dto';
import { Server } from 'socket.io';
import { Message } from './entities/message.entity';
import { Conversation } from './entities/conversation.entity';
import { TypingDto } from './dto/typing.dto';

@WebSocketGateway({ cors: '*' })
export class ConversationsGateway {
  @WebSocketServer()
  server: Server;

  constructor(private readonly conversationsService: ConversationsService) {}

  @SubscribeMessage('createConversation')
  async create(@MessageBody() createConversationDto: CreateConversationDto) {
    const conversation = await this.conversationsService.create(
      createConversationDto,
    );

    this.server.sockets.emit('new_conversation', conversation);
  }

  @SubscribeMessage('takeover')
  async handleTakeover(
    @MessageBody() updateConversationDto: UpdateConversationDto,
  ) {
    const takenover = await this.conversationsService.takeover(
      updateConversationDto.id,
      updateConversationDto,
    );

    this.server.sockets.emit('handle_takeover', takenover);
  }

  @SubscribeMessage('send_message')
  async listenForMessages(@MessageBody() message: Message) {
    const sendMessage = await this.conversationsService.addMessage(message);
    this.server.to(message.conversation).emit('receive_message', sendMessage);
  }

  @SubscribeMessage('is_typing')
  async listenForTyping(@MessageBody() typingDto: TypingDto) {
    this.server.to(typingDto.conversationId).emit('is_typing', typingDto);
  }
}
