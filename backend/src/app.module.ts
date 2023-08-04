import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { AuthModule } from './auth/auth.module';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { ConversationsModule } from './conversations/conversations.module';
import configuration from './config/configuration';
@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      load: [configuration],
    }),
    MongooseModule.forRootAsync({
      inject: [ConfigService],
      useFactory: async (configService: ConfigService) => ({
        uri: configService.get('databaseUrl'),
      }),
    }),
    AuthModule,
    ConversationsModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
