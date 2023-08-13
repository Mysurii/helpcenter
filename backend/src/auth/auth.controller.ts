import type { Request } from 'express';
import { User } from './schemas/user.schema';
import {
  Controller,
  Get,
  Post,
  Body,
  Delete,
  Req,
  BadRequestException,
} from '@nestjs/common';
import { AuthService } from './auth.service';
import { SignUpDto } from './dto/sign-up.dto';
import { SignInDto } from './dto/sign-in.dto';
import { Role, Roles } from './types/role';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('/signup')
  signUp(@Body() signUpDto: SignUpDto): Promise<{ token: string }> {
    return this.authService.signUp(signUpDto);
  }

  @Post('/signin')
  signIn(@Body() signInDto: SignInDto): Promise<{ token: string }> {
    return this.authService.signIn(signInDto);
  }

  @Get('/users')
  @Roles(Role.ADMIN)
  getPortalUsers(): Promise<User[]> {
    return this.authService.getPortalUsers();
  }

  @Delete('/users/:id')
  @Roles(Role.ADMIN)
  async delete(@Req() req: Request): Promise<{ deleted: boolean }> {
    const { id } = req.params;

    if (!id) throw new BadRequestException('Please provide a valid id');
    const deleted = await this.authService.deleteUser(id);
    return { deleted };
  }
}
