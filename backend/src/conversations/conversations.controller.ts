import { Controller, Get, NotFoundException, Req } from '@nestjs/common';
import { ConversationsService } from './conversations.service';
import { Request } from 'express';
import { Role, Roles } from 'src/auth/types/role';

@Controller('conversations')
export class ConversationsController {
  constructor(private conversationsService: ConversationsService) {}

  @Get('/:id')
  @Roles(Role.ADMIN)
  async getMessages(@Req() req: Request) {
    const { id } = req.params;

    const conversation = await this.conversationsService.fetchMessages(id);

    return conversation;
  }

  @Get()
  @Roles(Role.ADMIN)
  async getAllConversations() {
    const conversation = await this.conversationsService.findAll();

    return conversation;
  }
}
