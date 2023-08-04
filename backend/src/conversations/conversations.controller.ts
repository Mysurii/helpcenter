import { Controller, Get, NotFoundException, Req } from '@nestjs/common';
import { ConversationsService } from './conversations.service';
import { Request } from 'express';
import { Role, Roles } from 'src/auth/types/role';

@Controller('conversations')
export class ConversationsController {
  constructor(private conversationsService: ConversationsService) {}

  @Get()
  @Roles(Role.ADMIN)
  async getConversation(@Req() req: Request) {
    const { id } = req.params;

    const conversation = await this.conversationsService.findOne(id);

    if (!conversation) throw new NotFoundException('Conversation not found');

    return conversation;
  }

  @Get()
  @Roles(Role.ADMIN)
  async getAllConversations() {
    const conversation = await this.conversationsService.findAll();

    return conversation;
  }
}
