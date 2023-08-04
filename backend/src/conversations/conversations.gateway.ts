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

@WebSocketGateway()
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

  @SubscribeMessage('findOneConversation')
  async findOne(@MessageBody() id: string) {
    return await this.conversationsService.findOne(id);
  }

  @SubscribeMessage('takeover')
  async update(@MessageBody() updateConversationDto: UpdateConversationDto) {
    const takenover = await this.conversationsService.takeover(
      updateConversationDto.id,
      updateConversationDto,
    );

    this.server.sockets.emit('handle_takeover', takenover);
  }

  @SubscribeMessage('send_message')
  async listenForMessages(@MessageBody() message: Message) {
    const sendMessage = await this.conversationsService.addMessage(message);
    this.server.sockets.emit('receive_message', sendMessage);
  }
}
