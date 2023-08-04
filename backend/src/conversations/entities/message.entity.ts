import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';

@Schema({
  timestamps: true,
})
export class Message {
  @Prop()
  message: string;

  @Prop()
  sender: string;

  @Prop()
  conversationId: string;
}

export const MessageSchema = SchemaFactory.createForClass(Message);
