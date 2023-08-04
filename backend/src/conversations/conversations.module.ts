import { Module } from '@nestjs/common';
import { ConversationsService } from './conversations.service';
import { ConversationsGateway } from './conversations.gateway';
import { MongooseModule } from '@nestjs/mongoose';
import { MessageSchema } from './entities/message.entity';
import { ConversationSchema } from './entities/conversation.entity';
import { ConversationsController } from './conversations.controller';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: 'Message', schema: MessageSchema },
      { name: 'Conversation', schema: ConversationSchema },
    ]),
  ],
  providers: [ConversationsGateway, ConversationsService],
  controllers: [ConversationsController],
})
export class ConversationsModule {}
