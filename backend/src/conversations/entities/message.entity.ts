import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import mongoose, { Document } from 'mongoose';

@Schema({
  timestamps: true,
})
export class Message extends Document {
  @Prop()
  text: string;

  @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'User' })
  sender: string;

  @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'Conversation' })
  conversation: string;
}

export const MessageSchema = SchemaFactory.createForClass(Message);
