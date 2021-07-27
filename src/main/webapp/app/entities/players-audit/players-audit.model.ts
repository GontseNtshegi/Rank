import * as dayjs from 'dayjs';
import { IPlayers } from 'app/entities/players/players.model';

export interface IPlayersAudit {
  eventDate?: dayjs.Dayjs | null;
  operation?: string | null;
  winningMoney?: number | null;
  wageringMoney?: number | null;
  transactionId?: string;
  playerId?: number | null;
  promotion?: string | null;
  players?: IPlayers | null;
}

export class PlayersAudit implements IPlayersAudit {
  constructor(
    public eventDate?: dayjs.Dayjs | null,
    public operation?: string | null,
    public winningMoney?: number | null,
    public wageringMoney?: number | null,
    public transactionId?: string,
    public playerId?: number | null,
    public promotion?: string | null,
    public players?: IPlayers | null
  ) {}
}

export function getPlayersAuditIdentifier(playersAudit: IPlayersAudit): string | undefined {
  return playersAudit.transactionId;
}
