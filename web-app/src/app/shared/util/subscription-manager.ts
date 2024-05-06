import {Subscription} from "rxjs";

export class SubscriptionManager {
  private subscriptions: Subscription[] = [];

  public add(subscription: Subscription) {
    this.subscriptions.push(subscription);
  }

  public unsubcribeAll() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
