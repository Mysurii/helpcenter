import { Injectable, UnauthorizedException } from '@nestjs/common';
import { SignUpDto } from './dto/sign-up.dto';
import { InjectModel } from '@nestjs/mongoose';
import { User } from './schemas/user.schema';
import { Model } from 'mongoose';
import * as bcrypt from 'bcryptjs';
import { JwtService } from '@nestjs/jwt';
import { SignInDto } from './dto/sign-in.dto';

@Injectable()
export class AuthService {
  constructor(
    @InjectModel(User.name)
    private userModel: Model<User>,
    private jwtService: JwtService,
  ) {}

  async signUp(signUpDto: SignUpDto): Promise<{ token: string }> {
    const { name, email, password } = signUpDto;

    const hashedPassword = await bcrypt.hash(password, 10);

    const user = await this.userModel.create({
      name,
      email,
      password: hashedPassword,
    });

    const token = this.jwtService.sign({
      id: user._id,
      name: user.name,
      email: user.email,
    });

    return { token };
  }

  async signIn(signInDto: SignInDto): Promise<{ token: string }> {
    const { email, password } = signInDto;

    const user = await this.userModel.findOne({ email });

    if (!user) throw new UnauthorizedException('Invalid credentials');

    const isPasswordMatching = await bcrypt.compare(password, user.password);

    if (!isPasswordMatching)
      throw new UnauthorizedException('Invalid credentials');

    const token = this.jwtService.sign({
      id: user._id,
      name: user.name,
      email: user.email,
    });

    return { token };
  }
}
