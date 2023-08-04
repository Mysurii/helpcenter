import { PartialType } from '@nestjs/mapped-types';
import { Conversation } from '../entities/conversation.entity';
import { TStatus } from '../types/status.type';
import { IsNotEmpty, IsString } from 'class-validator';

export class UpdateConversationDto extends PartialType(Conversation) {
  @IsNotEmpty()
  @IsString()
  id: string;

  @IsNotEmpty()
  status: TStatus;
}
