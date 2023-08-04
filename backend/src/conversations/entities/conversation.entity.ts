import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { TStatus } from '../types/status.type';
import { TGroup } from '../types/group.type';
import { Document } from 'mongoose';

@Schema({
  timestamps: true,
})
export class Conversation extends Document {
  @Prop({ type: Object })
  members: TGroup;

  @Prop()
  status: TStatus;
}

export const ConversationSchema = SchemaFactory.createForClass(Conversation);
