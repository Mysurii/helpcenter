import { Injectable } from '@nestjs/common';
import { CreateConversationDto } from './dto/create-conversation.dto';
import { UpdateConversationDto } from './dto/update-conversation.dto';
import { Conversation } from './entities/conversation.entity';
import { InjectModel } from '@nestjs/mongoose';
import { Message } from './entities/message.entity';
import { Model, Types } from 'mongoose';

@Injectable()
export class ConversationsService {
  constructor(
    @InjectModel(Message.name)
    private messageModel: Model<Message>,
    @InjectModel(Conversation.name)
    private conversationModel: Model<Conversation>,
  ) {}

  async create(createConversationDto: CreateConversationDto) {
    const newConversation = {
      ...createConversationDto,
      status: 'unhandled',
    };

    const createdConversation = await this.conversationModel.create(
      newConversation,
    );

    return createdConversation;
  }

  async findAll() {
    return await this.conversationModel.find().populate({
      path: 'lastMessage',
      populate: {
        path: 'sender',
        model: 'User',
      },
    });
  }

  async findOne(id: string): Promise<{
    conversation: Conversation;
    messages: Message[];
  }> {
    const conversation = await this.conversationModel.findById(id);
    const messages = await this.messageModel.find({ conversationId: id });
    return { conversation, messages };
  }

  async takeover(id: string, updateConversationDto: UpdateConversationDto) {
    const updated = await this.conversationModel.findOneAndUpdate(
      { _id: id },
      updateConversationDto,
    );
    return updated;
  }

  async addMessage(message: Message) {
    return await this.messageModel.create(message);
  }

  async fetchMessages(id: string): Promise<Message[]> {
    console.log(id);
    const messages = await this.messageModel
      .find({
        conversation: new Types.ObjectId(id),
      })
      .populate('sender');

    return messages;
  }
}
