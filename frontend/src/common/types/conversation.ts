import { TMessage } from './message'

export type TConversation = {
  _id: string
  members: TGroup
  status: TStatus
  lastMessage: TMessage
}

export type TStatus = 'unhandles' | 'in progress' | 'done'

export type TGroup = {
  user: string
  employee: string
}
