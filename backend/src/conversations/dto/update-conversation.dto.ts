import { PartialType } from '@nestjs/mapped-types';
import { Conversation } from '../entities/conversation.entity';
import { TStatus } from '../types/status.type';

export class UpdateConversationDto extends PartialType(Conversation) {
  id: string;
  status: TStatus;
}
